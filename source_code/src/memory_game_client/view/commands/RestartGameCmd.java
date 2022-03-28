package memory_game_client.view.commands;

import memory_game_client.view.GameFrame;

/**
 * Disposes the current GameFrame window and opens a new one upon execution.
 */
public class RestartGameCmd implements ICommand {

    private GameFrame gameFrame;
    private String playerName;

    public RestartGameCmd(GameFrame gameFrame, String playerName) {
        this.gameFrame = gameFrame;
        this.playerName = playerName;
    }

    @Override
    public void execute() {
        this.gameFrame.dispose();
        new GameFrame(playerName, null, false);
    }
}
