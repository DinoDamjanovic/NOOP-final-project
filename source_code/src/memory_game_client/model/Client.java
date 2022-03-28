package memory_game_client.model;

import memory_game_client.controller.Controller;

import java.io.*;
import java.net.Socket;

/**
 * This class is responsible for communication with the multiplayer server.
 * <p>
 *     All the received data is passed on to the controller for further processing.
 * </p>
 * @see Controller
 */
public class Client {

    private Controller controller;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private int[][] imagePairs;
    private int[] openedPair;

    public Client(Controller controller, Socket socket, String playerUsername) {
        this.controller = controller;
        this.socket = socket;


        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.outputStream.writeObject(playerUsername);
            this.outputStream.flush();

        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }


    /**
     * Listens for incoming data from the server. Upon receiving an Object via ObjectInputStream,
     * passes it to the controller for further processing.
     */
    public void startClient() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Object data;

                try {
                    while (socket.isConnected()) {
                        data = inputStream.readObject();

                        if (data instanceof int[]) {
                            openedPair = (int[]) data;
                            controller.setOpenedPair(openedPair);
                            controller.playerTurn();

                        } else if (data instanceof Boolean) {
                            controller.processTurnToPlay((Boolean) data);

                        } else if (data instanceof String) {
                            controller.setPlayer2Name((String) data);

                        } else if (data instanceof Integer) {
                            processRematchCode((Integer) data);

                        } else if (data instanceof int[][]) {
                            imagePairs = (int[][]) data;
                            controller.refreshGame();
                            controller.setImagePairs(imagePairs);
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    closeEverything(socket, inputStream, outputStream);
                }
            }
        }).start();
    }

    /**
     * Sends back the response to the rematch code received.
     * <p>
     *     0 - rematchAccepted
     *     -1, 1, 2 - rematchResponse - No <br>
     * </p>
     * */
    private void processRematchCode(int rematchCode) {
        int response = controller.processRematchCode(rematchCode);
        switch (response) {
            case 0:
            case -1:
            case 1:
            case 2:
                sendData(Integer.valueOf(response));
                break;
        }
    }

    public void sendData(Object data) {
        try {
            if (socket.isConnected()) {
                outputStream.writeObject(data);
                outputStream.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, inputStream, outputStream);
        }
    }

    /**
     * Closes all the opened streams and terminates the connection with the server.
     * @param socket Opened socket to close
     * @param inputStream Opened input stream to close
     * @param outputStream Opened output stream to close
     */
    private void closeEverything(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {

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
    }

    public void sendOpenedPair(int[] openedPair) {
        sendData(openedPair);
    }

    public void sendRematchRequest() {
        sendData(Integer.valueOf(3));
    }
}
