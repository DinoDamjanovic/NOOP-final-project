package memory_game_server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI for Memory Game server
 * <p>
 *     Shows information about the server gaming rooms current status
 *     (name(s) of user(s) currently in the rooms).
 * </p>
 *     Also provides functionality of shutting down the server via Shut Down button or by closing the window.
 */
public class ServerControlFrame extends JFrame {

    private static final int NUM_OF_GAME_ROOMS = 10;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 470;

    private ServerController serverController;

    private JButton shutDownButton;
    private JLabel runningInfo;
    private JLabel[] roomInfo;
    private ArrayList<String>[] playerNames;

    public ServerControlFrame() {
        super("MemoryGame Server");

        this.serverController = new ServerController(this);

        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon image = new ImageIcon("icons/frameIcon.png");
        this.setIconImage(image.getImage());

        initializeComponents();
        setLayout();
        this.getContentPane().setBackground(new Color(235, 247, 255));
        setVisible(true);
    }

    private void initializeComponents() {
        Dimension dimension = new Dimension(300, 60);
        Font font = new Font("Calibri", Font.BOLD, 28);
        Font fontSmall = new Font("Calibri", Font.PLAIN, 16);
        Color buttonColor = new Color(216, 109, 109);
        shutDownButton = new JButton("Shut Down Server");
        shutDownButton.setActionCommand("exit");
        shutDownButton.setVerticalAlignment(SwingConstants.BOTTOM);
        shutDownButton.setFocusable(false);
        shutDownButton.setPreferredSize(dimension);
        shutDownButton.setFont(font);
        shutDownButton.setBackground(buttonColor);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverController.closeServer();
                System.exit(0);
            }
        };
        shutDownButton.addActionListener(actionListener);

        runningInfo = new JLabel("Server Running");
        runningInfo.setFont(font);

        roomInfo = new JLabel[NUM_OF_GAME_ROOMS];
        playerNames = new ArrayList[NUM_OF_GAME_ROOMS];

        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = new ArrayList<String>(2);
        }

        for (int i = 0; i < NUM_OF_GAME_ROOMS; i++) {
            roomInfo[i] = new JLabel();
            roomInfo[i].setFont(fontSmall);
            roomInfo[i].setText("Game Room " + (i + 1) + ": Empty");
        }

    }

    private void setLayout() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));

        this.add(runningInfo);

        for (JLabel room : roomInfo) {
            this.add(room);
        }
        this.add(shutDownButton);
    }

    public void startServer() {
        serverController.startServer();
    }

    /**
     * Used by the ServerController to update the information about player
     * joining the specific Game room in server GUI.
     */
    public void playerJoined(String playerUsername, int roomNumber) {
        playerNames[roomNumber - 1].add(playerUsername);

        if (playerNames[roomNumber - 1].size() == 1) {
            roomInfo[roomNumber - 1].setText("Game Room " + roomNumber +
                    ": " + playerUsername);

        } else {
            roomInfo[roomNumber - 1].setText("Game Room " + roomNumber +
                    ": " + playerNames[roomNumber - 1].get(0) + " VS " + playerUsername);
        }
    }

    /**
     * Used by the ServerController to update the information about player
     * leaving the specific Game room in server GUI.
     */
    public void playerLeft(String playerUsername, int roomNumber) {
        playerNames[roomNumber - 1].remove(playerUsername);

        if (playerNames[roomNumber - 1].size() == 1) {
            roomInfo[roomNumber - 1].setText("Game Room " + roomNumber +
                    ": " + playerNames[roomNumber - 1].get(0));

        } else {
            roomInfo[roomNumber - 1].setText("Game Room " + roomNumber + ": Empty");
        }
    }
}
