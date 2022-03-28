package memory_game_client.view.logInRegistration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Abstract class which provides a skeleton for LogInForm and RegistrationForm classes.
 * Defines font and size for the form label, login and password fields, and also for the action button.
 * @see LogInForm LoginForm
 * @see RegistrationForm RegistrationForm
 */
public abstract class UserForm extends JPanel implements ActionListener {
    protected JLabel infoLabel;

    protected JLabel usernameLabel;
    protected JTextField usernameField;

    protected JLabel passwordLabel;
    protected JPasswordField passwordField;
    protected JButton actionButton;

    protected Font font = new Font("Calibri", Font.BOLD, 16);
    protected Dimension textFieldSize = new Dimension(180, 30);

    UserForm(String panelInfoText, String actionButtonText) {
        Dimension dimensions = getPreferredSize();
        dimensions.width = 325;
        Color buttonColor = new Color(255, 255, 255);
        setPreferredSize(dimensions);
        setBackground(new Color(235, 247, 255));

        infoLabel = new JLabel(panelInfoText);
        infoLabel.setPreferredSize(new Dimension(260, 20));
        infoLabel.setFont(font);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(font);
        usernameField = new JTextField();
        usernameField.setPreferredSize(textFieldSize);

        passwordLabel = new JLabel(" Password:");
        passwordLabel.setFont(font);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(textFieldSize);

        actionButton = new JButton(actionButtonText);
        actionButton.setPreferredSize(new Dimension(100, 30));
        actionButton.addActionListener(this);
        actionButton.setBackground(buttonColor);
    }
}
