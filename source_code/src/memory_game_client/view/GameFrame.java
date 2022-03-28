package memory_game_client.view;

import memory_game_client.controller.Controller;
import memory_game_client.model.Client;
import memory_game_client.view.commands.ICommand;
import memory_game_client.view.commands.RematchCmd;
import memory_game_client.view.jobs.ShowHighScoresJob;
import memory_game_client.view.jobs.ShowUserScoresJob;
import memory_game_client.view.commands.RestartGameCmd;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;


/**
 * Main game frame.
 */
public class GameFrame extends JFrame {

    private Controller controller;
    private Client client;

    private MenuPanel menuPanel;
    private BoardPanel boardPanel;
    private ScorePanel scorePanel;

    public GameFrame(String playerName, String serverIpAddress, boolean isMultiplayer) {

        this.controller = new Controller();
        this.boardPanel = new BoardPanel(isMultiplayer);

        this.scorePanel = new ScorePanel(isMultiplayer);
        this.scorePanel.setPlayer1Name(playerName);

        if (isMultiplayer) {
            this.scorePanel.setActionButtonCommand(new RematchCmd(controller));
        } else {
            this.scorePanel.setActionButtonCommand(new RestartGameCmd(this, playerName));
        }

        this.controller.setUsername(playerName);
        this.controller.setBoardPanel(boardPanel);
        this.controller.setScorePanel(scorePanel);
        this.boardPanel.setController(controller);

        if (isMultiplayer) {
            try {
                this.client = new Client(controller, new Socket(serverIpAddress, 1236), playerName);
                this.controller.setClient(this.client);
                this.client.startClient();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null, "Unable to connect to Multiplayer Server.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        ICommand restartCommand;
        if (isMultiplayer) {
            restartCommand = new RematchCmd(controller);
        } else {
            restartCommand = new RestartGameCmd(this, playerName);
        }

        this.menuPanel = new MenuPanel(
                restartCommand,
                new ShowUserScoresJob(controller),
                new ShowHighScoresJob(controller),
                isMultiplayer);

        this.setLayout(new BorderLayout());
        this.add(menuPanel, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.WEST);
        this.add(scorePanel, BorderLayout.EAST);

        this.setTitle("Memory Game");
        ImageIcon image = new ImageIcon("icons/frameIcon.png");
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

