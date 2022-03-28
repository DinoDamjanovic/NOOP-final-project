package memory_game_client.view.logInRegistration;

import java.util.EventObject;

/**
 * An object used to store user registration information (username, password and confirmation password).
 * This object is instantiated in the RegistrationForm class.
 * @see RegistrationForm RegistrationForm
 */
public class RegistrationEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public RegistrationEvent(Object source) {
        super(source);
    }

    private String username;
    private char[] password;
    private char[] passwordConfirmation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(char[] passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
