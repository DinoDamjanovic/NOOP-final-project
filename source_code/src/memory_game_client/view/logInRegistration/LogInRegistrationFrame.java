package memory_game_client.view.logInRegistration;

import memory_game_client.controller.Controller;
import memory_game_client.view.GameModeFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Window which contains user login and registration forms.
 * The information collected in these forms is passed on to the Controller for further processing.
 *
 * @see Controller Controller
 */
public class LogInRegistrationFrame extends JFrame {

    private static final int WIDTH = 670;
    private static final int HEIGHT = 250;

    private Controller controller;
    private LogInForm logInForm;
    private RegistrationForm registrationForm;
    private FormListener formListener;

    public LogInRegistrationFrame() {
        super("Memory Game");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 255, 255));

        ImageIcon image = new ImageIcon("icons/frameIcon.png");
        setIconImage(image.getImage());

        initializeFrame();
        setFrameLayout();
        setVisible(true);
    }

    private void initializeFrame() {
        controller = new Controller();
        logInForm = new LogInForm();
        registrationForm = new RegistrationForm();

        formListener = new FormListener() {
            @Override
            public void logInEventOccured(LogInEvent logInEvent) {

                int resultCode = controller.userLogin(logInEvent);
//                Comment line above and uncomment line below to disable login check
//                int resultCode = 1;

                if (resultCode == 1) {
                    dispose();
                    GameModeFrame gameModeFrame = new GameModeFrame();
                    gameModeFrame.setPlayerName(logInEvent.getUsername());

                } else if (resultCode == 0) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this, "Invalid username or password!",
                            "User not found", JOptionPane.WARNING_MESSAGE);

                } else if (resultCode == -1) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this,
                            "Unable to establish connection with a server. Please try again!",
                            "Unable to reach server", JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            public void registrationEventOccured(RegistrationEvent registrationEvent) {
                if (Arrays.compare(registrationEvent.getPassword(), registrationEvent.getPasswordConfirmation()) != 0) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this, "Passwords do not match!",
                            "Wrong confirmation password", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int resultCode = controller.userRegistration(registrationEvent);

                if (resultCode == 1) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this, "Registration successful! Log in to continue.",
                            "Registration complete", JOptionPane.PLAIN_MESSAGE);
                    registrationForm.clearFields();

                } else if (resultCode == 0) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this,
                            "User with username " + registrationEvent.getUsername() + " is already registered!",
                            "Username taken", JOptionPane.WARNING_MESSAGE);

                } else if (resultCode == -1) {
                    JOptionPane.showMessageDialog(LogInRegistrationFrame.this,
                            "Unable to establish connection with a server. Please try again!",
                            "Unable to reach server", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        logInForm.setFormListener(formListener);
        registrationForm.setFormListener(formListener);
    }

    private void setFrameLayout() {
        add(logInForm, BorderLayout.WEST);
        add(registrationForm, BorderLayout.EAST);
    }
}
