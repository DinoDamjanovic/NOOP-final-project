package memory_game_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server client for Memory Game.
 * <p>
 *    Waits for clients to connect. Upon new client connection, creates a new Thread and
 *    passes the client to ClientHandler job assigned to a newly created Thread for further processing.
 * </p>
 * @see ClientHandler
 */
public class MultiplayerServer {

    private ServerSocket serverSocket;
    private ServerController serverController;

    public MultiplayerServer(ServerController serverController) {
        try {
            this.serverSocket = new ServerSocket(1236);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverController = serverController;
    }

    public void startServer() {

        System.out.println("______________________________________");
        System.out.println("MemoryGame multiplayer server started.");
        System.out.println("IP address: " + serverSocket.getInetAddress());
        System.out.println("Port: " + serverSocket.getLocalPort());
        System.out.println("______________________________________");
        try {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                new Thread(new ClientHandler(socket, serverController)).start();
            }

        } catch (IOException e) {
            closeServer();
        }
    }

    public void closeServer() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
