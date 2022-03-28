package memory_game_client.view;

import memory_game_client.view.jobs.ShowHighScoresJob;
import memory_game_client.view.jobs.ShowUserScoresJob;
import memory_game_client.view.commands.ICommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Menu panel which is located in top left corner of the main window.
 */
public class MenuPanel extends JPanel {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem restart, highScores, myScores, exit;
    private ICommand restartCommand;
    private ShowUserScoresJob showUserScoresJob;
    private ShowHighScoresJob showHighScoresJob;

    public MenuPanel(ICommand restartCommand,
                     ShowUserScoresJob showUserScoresJob,
                     ShowHighScoresJob showHighScoresJob,
                     boolean isMultiplayer) {

        this.restartCommand = restartCommand;
        this.showUserScoresJob = showUserScoresJob;
        this.showHighScoresJob = showHighScoresJob;

        this.setBackground(new Color(255, 255, 255));

        menuBar = new JMenuBar();
        menu = new JMenu("Menu");

        restart = new JMenuItem();
        restart.setText("Restart game");

        restart.setActionCommand("Restart");

        highScores = new JMenuItem("Show high scores");
        highScores.setActionCommand("HighScores");

        myScores = new JMenuItem("Show my scores");
        myScores.setActionCommand("MyScores");

        exit = new JMenuItem("Exit game");
        exit.setActionCommand("Exit");

        if (!isMultiplayer) {
            menu.add(restart);
        }

        menu.add(highScores);
        menu.add(myScores);
        menu.add(new JSeparator());
        menu.add(exit);

        menuBar.add(menu);

        setLayout(new BorderLayout());
        add(menuBar, BorderLayout.WEST);

        activateMenu();
    }

    private void activateMenu() {

        ActionListener menuActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int response;
                switch (e.getActionCommand()) {
                    case "Restart":
                        response = JOptionPane.showConfirmDialog(
                                null, "Restart game?");
                        if (response == 0) {
                            restartCommand.execute();
                        }
                        break;

                    case "HighScores":
                        showHighScoresJob.execute();
                        break;

                    case "MyScores":
                        showUserScoresJob.execute();
                        break;

                    case "Exit":
                        response = JOptionPane.showConfirmDialog(
                                null, "Exit game?");
                        if (response == 0) {
                            System.exit(0);
                        }
                        break;

                }
            }
        };

        restart.addActionListener(menuActionListener);
        highScores.addActionListener(menuActionListener);
        myScores.addActionListener(menuActionListener);
        exit.addActionListener(menuActionListener);

        // add accelerators and mnemonics
        menu.setMnemonic(KeyEvent.VK_Q);

        restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.SHIFT_MASK));
        highScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
        myScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.SHIFT_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
    }
}
