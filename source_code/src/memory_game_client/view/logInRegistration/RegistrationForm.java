package memory_game_client.view.logInRegistration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Used to collect, store and pass on user registration information
 * (username, password and password confirmation) for further processing.
 */
public class RegistrationForm extends UserForm {

    private JLabel confirmPasswordLabel;
    private JPasswordField confirmPasswordField;
    private FormListener formListener;

    RegistrationForm() {
        super("Register:", "Register");
        confirmPasswordLabel = new JLabel("   Confirm:");
        confirmPasswordLabel.setFont(font);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(textFieldSize);

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        add(infoLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(confirmPasswordLabel);
        add(confirmPasswordField);
        add(actionButton);
    }

    public void setFormListener(FormListener formListener) {
        this.formListener = formListener;
    }

    /**
     * Clears all text fields in this form.
     */
    public void clearFields() {
        usernameField.setText(null);
        passwordField.setText(null);
        confirmPasswordField.setText(null);
    }

    /**
     * Activated upon pressing the action button in this form.
     * If all fields in this form are filled, RegistrationEvent object is instantiated, registration information is
     * stored inside, and it is passed on to the FormListener object for further processing.<br>
     * If one or more fields are empty, dialog with appropriate message is displayed.
     * @see RegistrationEvent RegistrationEvent
     * @see LogInRegistrationFrame LogInRegistrationFrame
     * @see FormListener FormListener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == actionButton) {
            if (!usernameField.getText().isEmpty() && (passwordField.getPassword().length != 0) &&
                    (confirmPasswordField.getPassword().length != 0)) {
                RegistrationEvent registrationEvent = new RegistrationEvent(this);

                registrationEvent.setUsername(usernameField.getText());
                registrationEvent.setPassword(passwordField.getPassword());
                registrationEvent.setPasswordConfirmation(confirmPasswordField.getPassword());
                formListener.registrationEventOccured(registrationEvent);
            } else {
                JOptionPane.showMessageDialog(null, "All fields are required!",
                        "Invalid input", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
