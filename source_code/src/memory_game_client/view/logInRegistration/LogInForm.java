package memory_game_client.view.logInRegistration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Used to collect, store and pass on user login information (username and password) for further processing.
 */
public class LogInForm extends UserForm {

    private FormListener formListener;

    LogInForm() {
        super("Sign in:", "Sign in");

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        add(infoLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(actionButton);
    }

    public void setFormListener(FormListener formListener) {
        this.formListener = formListener;
    }

    /**
     * Activated upon pressing the action button in this form.
     * If all fields in this form are filled, LogInEvent object is instantiated, login information is
     * stored inside and the object is passed to the FormListener object for further processing.<br>
     * If one or more fields are empty, dialog with appropriate message is displayed.
     * @see LogInEvent LogInEvent
     * @see LogInRegistrationFrame LogInRegistrationFrame
     * @see FormListener FormListener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == actionButton) {
            if (!usernameField.getText().isEmpty() && (passwordField.getPassword().length != 0)) {
                LogInEvent logInEvent = new LogInEvent(this);

                logInEvent.setUsername(usernameField.getText());
                logInEvent.setPassword(passwordField.getPassword());
                formListener.logInEventOccured(logInEvent);
            } else {
                JOptionPane.showMessageDialog(null, "Username and password required!",
                        "Invalid input", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
