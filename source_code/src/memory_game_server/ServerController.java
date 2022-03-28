package memory_game_server;

public class ServerController {

    private MultiplayerServer multiplayerServer;
    private ServerControlFrame serverControlFrame;

    public ServerController(ServerControlFrame serverControlFrame) {
        this.serverControlFrame = serverControlFrame;

        this.multiplayerServer = new MultiplayerServer(this);
    }

    public void startServer() {
        multiplayerServer.startServer();
    }

    public void closeServer() {
        multiplayerServer.closeServer();
    }

    public void playerJoined(String playerUsername, int gameRoomNumber) {
        serverControlFrame.playerJoined(playerUsername, gameRoomNumber);
    }

    public void playerLeft(String playerUsername, int gameRoomNumber) {
        serverControlFrame.playerLeft(playerUsername, gameRoomNumber);
    }
}
