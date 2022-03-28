package memory_game_client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Game mode frame - offers the client the choice between Single player and multiplayer mode.<br>
 * Also contains the text input field for multiplayer server's IP address.
 */
public class GameModeFrame extends JFrame {

    private static final int WIDTH = 670;
    private static final int HEIGHT = 340;

    private String playerName;
    private JButton singlePlayer;
    private JButton multiPlayer;
    private JLabel ipInputLabel;
    private JTextField ipAddressInput;
    private JLabel ipAddressInfo;
    private JLabel ipAddressInfo2;
    private String serverIpAddress;

    public GameModeFrame() {
        super("Memory Game");
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
        Dimension dimension = new Dimension(600, 60);
        Font font = new Font("Calibri", Font.BOLD, 28);
        Font fontSmallItalic = new Font("Verdana", Font.ITALIC, 14);
        Font fontSmall = new Font("Verdana", Font.PLAIN, 14);
        Color buttonColor = new Color(255, 255, 255);
        singlePlayer = new JButton("Single Player");
        singlePlayer.setActionCommand("Single");
        singlePlayer.setVerticalAlignment(SwingConstants.BOTTOM);
        singlePlayer.setFocusable(false);
        singlePlayer.setPreferredSize(dimension);
        singlePlayer.setFont(font);
        singlePlayer.setBackground(buttonColor);

        multiPlayer = new JButton("Versus Opponent");
        multiPlayer.setActionCommand("Multiplayer");
        multiPlayer.setVerticalAlignment(SwingConstants.BOTTOM);
        multiPlayer.setFocusable(false);
        multiPlayer.setPreferredSize(dimension);
        multiPlayer.setFont(font);
        multiPlayer.setBackground(buttonColor);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch (e.getActionCommand()) {
                    case "Single":
                        dispose();
                        new GameFrame(playerName, null, false);
                        break;
                    case "Multiplayer":
                        dispose();
                        serverIpAddress = ipAddressInput.getText();
                        serverIpAddress = serverIpAddress.strip();
                        new GameFrame(playerName, serverIpAddress, true);
                        break;
                }
            }
        };
        singlePlayer.addActionListener(actionListener);
        multiPlayer.addActionListener(actionListener);

        ipInputLabel = new JLabel("Server IP address: ");
        ipInputLabel.setFont(fontSmall);
        ipAddressInput = new JTextField();
        ipAddressInput.setFont(fontSmall);
        ipAddressInput.setPreferredSize(new Dimension(150, 20));

        ipAddressInfo = new JLabel("Please start the MemoryGameServer on your local network to play Versus Opponent.");
        ipAddressInfo2 = new JLabel("Open Command prompt and type ipconfig to find out IP address of the server.");
        ipAddressInfo.setFont(fontSmallItalic);
        ipAddressInfo2.setFont(fontSmallItalic);
    }

    private void setLayout() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        this.add(singlePlayer);
        this.add(multiPlayer);

        this.add(ipInputLabel);
        this.add(ipAddressInput);
        this.add(ipAddressInfo);
        this.add(ipAddressInfo2);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
