package memory_game_client.view;

import memory_game_client.view.commands.ICommand;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays score(s) for the player(s) and information about game outcome.
 * In multiplayer mode also displays whose turn it is.<br>
 * Also, displays the Restart game (single player) or Rematch (multiplayer) button.
 */
public class ScorePanel extends JPanel implements ActionListener {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 750;

    private boolean isMultiplayer;

    private Font font;
    private Font fontMedium;

    private JLabel player1Label;
    private JLabel player2Label;

    private JLabel player1Name;
    private JLabel player2Name;

    private JLabel player1Score;
    private JLabel attempts;
    private JLabel player2Score;

    private JLabel playerTurnLabel;

    private JLabel gameFinishedText;
    private JLabel performanceScoreText;

    private JButton actionButton;

    private ICommand actionButtonCommand;

    private Timer timer;

    ScorePanel(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
        initializeComps();
        setLayout();
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name.setText(player1Name);
    }

    private void initializeComps() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(235, 247, 255));
        this.setFocusable(true);

        Border raisedbevel, loweredbevel;
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();

        Border compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
        this.setBorder(compound);

        player1Label = new JLabel("Player 1");
        player2Label = new JLabel("Player 2");

        player1Name = new JLabel("");
        player2Name = new JLabel(" Waiting...");

        player1Label.setBorder(compound);
        player2Label.setBorder(compound);

        player1Score = new JLabel("Score: 0");
        player2Score = new JLabel("Score: 0");
        attempts = new JLabel("Attempts: 0");

        playerTurnLabel = new JLabel("");

        gameFinishedText = new JLabel("");
        performanceScoreText = new JLabel("");

        actionButton = new JButton();
        if (isMultiplayer) {
            actionButton.setText("Rematch");

            timer = new Timer(0, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionButton.setEnabled(true);
                }
            });
            timer.setRepeats(false);
            timer.setInitialDelay(15000);

        } else {
            actionButton.setText("Restart game");
        }

        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Name.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Name.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Score.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Score.setAlignmentX(Component.CENTER_ALIGNMENT);
        attempts.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerTurnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameFinishedText.setAlignmentX(Component.CENTER_ALIGNMENT);
        performanceScoreText.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        font = new Font("Verdana", Font.BOLD, 20);
        fontMedium = new Font("Verdana", Font.BOLD, 30);
        player1Label.setFont(font);
        player2Label.setFont(font);

        player1Name.setFont(fontMedium);
        player2Name.setFont(fontMedium);

        player1Score.setFont(font);
        player2Score.setFont(font);
        attempts.setFont(font);

        playerTurnLabel.setFont(fontMedium);

        gameFinishedText.setFont(fontMedium);
        performanceScoreText.setFont(font);

        actionButton.setVerticalAlignment(SwingConstants.BOTTOM);
        actionButton.setFont(fontMedium);
        actionButton.setFocusable(false);
        actionButton.setBackground(new Color(255, 255, 255));
        actionButton.addActionListener(this);
    }

    private void setLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(Box.createVerticalStrut(120));
        this.add(gameFinishedText);
        if (!isMultiplayer) {
            this.add(performanceScoreText);
        }
        this.add(player1Label);
        this.add(Box.createVerticalStrut(10));
        this.add(player1Name);
        this.add(player1Score);
        this.add(attempts);

        if (isMultiplayer) {
            this.add(Box.createVerticalStrut(100));
            this.add(player2Label);
            this.add(Box.createVerticalStrut(10));
            this.add(player2Name);
            this.add(player2Score);

            this.add(Box.createVerticalStrut(130));
            this.add(playerTurnLabel);
            this.add(Box.createVerticalStrut(30));
            this.add(actionButton);
        } else {
            this.add(Box.createVerticalStrut(400));
            this.add(actionButton);
        }
    }

    public void setActionButtonCommand(ICommand actionButtonCommand) {
        this.actionButtonCommand = actionButtonCommand;
    }

    public void setPlayer1Score(int score) {
        player1Score.setText("Score: " + score);
    }

    public void setPlayer2Score(int score) {
        player2Score.setText("Score: " + score);
    }

    public void setAttempts(int count) {
        attempts.setText("Attempts: " + count);
    }

    public void setGameFinishedText() {
        gameFinishedText.setText("Game Finished");

        player1Label.setText("");
        player1Label.setBorder(null);
        player1Name.setText("");
        player1Score.setText("");
    }

    public void setGameFinishedText(int player) {
        String text = "";

        switch (player) {
            case 0:
                text = "Tie!";
                break;
            case 1:
                text = player1Name.getText() + " wins!";
                break;
            case 2:
                text = player2Name.getText() + " wins!";
                break;
        }

        playerTurnLabel.setText(text);
    }


    public void setPlayer1PerformanceScore(int performanceScore) {
        performanceScoreText.setText("Performance score: " + performanceScore);
    }

    public void setPlayer2Name(String name) {
        player2Name.setText(name);
    }

    public void playerTurn() {
        playerTurnLabel.setText("Your turn");
    }

    public void opponentTurn() {
        playerTurnLabel.setText(player2Name.getText() + "'s turn");
    }

    /**
     * Executed upon pressing Restart game/Rematch button.
     * <p>
     *     If user confirms the Dialog (code 0), executes the actionButtonCommand.
     *     In multiplayer mode, button is disabled and timer is started which enables the button
     *     again after time delay set by timer.setInitialDelay() method.
     *     This is to prevent spamming the opponent with rematch requests.
     * </p>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(
                null, isMultiplayer ? "Request Rematch?" : "Restart game?");
        if (response == 0) {
            actionButtonCommand.execute();

            if (isMultiplayer) {
                actionButton.setEnabled(false);
                timer.start();
            }
        }
    }

    public void refreshGame() {
        player1Score.setText("Score: 0");
        player2Score.setText("Score: 0");
    }
}
