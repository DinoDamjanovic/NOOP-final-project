package memory_game_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Communicates with the client which was assigned to this specific ClientHandler.
 * Also, a pair of ClientHandlers work together to transfer data between
 * two clients who are playing versus each other.
 */
public class ClientHandler implements Runnable {

    private static final int NUM_OF_CARDS = 20;
    private static final int NUM_OF_GAME_ROOMS = 10;
    private static final ArrayList<ArrayList<ClientHandler>> gameRooms = new ArrayList<>(NUM_OF_GAME_ROOMS);

    private final Socket socket;
    private final ServerController serverController;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String playerUsername;
    private ArrayList<ClientHandler> gameRoom;
    private int gameRoomNumber;

    public ClientHandler(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());

            this.playerUsername = getPlayerName();

            addPlayerToGameRoom();

        } catch (IOException e) {
            closeEverything();
        }
    }

    /**
     * Waits for the incoming data from the client.
     * <p>
     *     Data is an Object which is then processed based on the type of the Object instance received.
     * </p>
     */
    @Override
    public void run() {
        Object data;

        try {
            while (socket.isConnected()) {
                data = inputStream.readObject();

                if (data instanceof int[]) {
                    int[] openedPair = (int[]) data;
                    sendOpenedPair(openedPair);

                } else if (data instanceof Integer) {
                    processRematchCode((Integer) data);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            closeEverything();
        }
    }

    /**
     * Initializes a new int[][] array which holds the information about the randomly generated card pairs.
     * This array is then passed to both clients who are starting the match versus each other.
     * Next, sends each client the name of his opponent.
     * Finally, a random number is generated to determine which client plays first and then
     * both clients are notified about it.
     */
    private void startGame() {
        int [][] imagePairs = generatePairs();
        System.out.println();
        for (ClientHandler clientHandler : gameRoom) {
            clientHandler.sendData(imagePairs);
            clientHandler.sendOpponentName();
        }

        int playerTurn = ThreadLocalRandom.current().nextInt(2);
        gameRoom.get(playerTurn).playerTurn();
        gameRoom.get(playerTurn).broadcastItsMyTurn();
    }


    /**
     * Sends an int array which holds indexes of opened pair of cards on the board
     * to the opposing client.
     * @param openedPair int array which holds indexes of opened pairs of cards
     */
    private void sendOpenedPair(int[] openedPair) {
        for (ClientHandler clientHandler : gameRoom) {
            if (clientHandler != this) {
                clientHandler.sendData(openedPair);
                break;
            }
        }
    }

    private String getPlayerName() {
        String playerName = null;

        try {
            playerName = (String) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            closeEverything();
        }
        return playerName;
    }


    /**
     * Adds this ClientHandler to the GameRoom. GameRoom is represented by an ArrayList of
     * two ClientHandler objects, each serving one client.
     */
    private void addPlayerToGameRoom() {
        if (gameRooms.isEmpty()) {
            for (int i = 0; i < NUM_OF_GAME_ROOMS; i++) {
                gameRooms.add(new ArrayList<>(2));
            }
        }

        int counter = 1;
        for (ArrayList<ClientHandler> gameRoom : gameRooms) {
            if (gameRoom.size() < 2) {
                gameRoom.add(this);
                this.gameRoom = gameRoom;
                this.gameRoomNumber = counter;

                System.out.println("+ " + playerUsername + " has joined Game Room " + counter);
                serverController.playerJoined(playerUsername, gameRoomNumber);

                if (gameRoom.size() == 2) {
                    startGame();
                }
                break;
            }
            counter++;
        }
    }

    public void sendData(Object data) {
        try {
            if (socket.isConnected()) {
                outputStream.writeObject(data);
                outputStream.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }


    private void sendOpponentName() {
        for (ClientHandler clientHandler : gameRoom) {
            if (clientHandler != this) {
                clientHandler.sendData(playerUsername);
                break;
            }
        }
    }

    /**
     * Sends a boolean true value to the client which is interpreted as a client's turn to play.
     */
    public void playerTurn() {
        sendData(true);
    }


    /**
     * Sends a boolean false value to the opposing client which is interpreted as a not his turn to play.
     */
    public void broadcastItsMyTurn() {
        for (ClientHandler clientHandler : gameRoom) {
            if (clientHandler != this) {
                clientHandler.sendData(Boolean.valueOf(false));
                break;
            }
        }
    }

    /**
     * Processes the rematch code received.
     *<p>
     * 3 - rematchRequest <br>
     * 0 - rematchResponse - Yes <br>
     * -1, 1, 2 - rematchResponse - No <br>
     * </p>
     * */
    private void processRematchCode(int rematchCode) {

        switch (rematchCode) {
            case 0:
                startGame();
                break;
            case -1:
            case 1:
            case 2:
            case 3:
                sendRematchCode(rematchCode);
                break;
        }
    }

    private void sendRematchCode(Integer rematchCode) {
        for (ClientHandler clientHandler : gameRoom) {
            if (clientHandler != this) {
                clientHandler.sendData(rematchCode);
            }
        }
    }

    /**
     * Generates an array of int[2] arrays which represent the card pairs for the board.
     * @return array of randomly generated pairs of integers
     */
    private int[][] generatePairs() {
        Stack<Integer> indexes = new Stack<>();
        for (int i = 0; i < NUM_OF_CARDS; i++) {
            indexes.push(i);
        }
        Collections.shuffle(indexes);

        int[][] imagePairs = new int[NUM_OF_CARDS / 2][2];
        for (int[] pair : imagePairs) {
            pair[0] = indexes.pop();
            pair[1] = indexes.pop();
        }
        return imagePairs;
    }

    public void removeClientHandler() {
        for (ArrayList<ClientHandler> gameRoom : gameRooms) {
            gameRoom.remove(this);
        }
    }

    /**
     * Closes the connection with the client and removes the instance of this ClientHandler
     * from the ArrayList of active ClientHandlers, making it eligible for garbage collection.
     */
    public void closeEverything() {
        removeClientHandler();

        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("- " + playerUsername + " has left Game Room " + gameRoomNumber);
        serverController.playerLeft(playerUsername, gameRoomNumber);
    }
}
