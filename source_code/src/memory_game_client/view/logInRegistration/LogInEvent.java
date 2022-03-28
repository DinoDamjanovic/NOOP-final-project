package memory_game_client.view.logInRegistration;

import java.util.EventObject;

/**
 * An object used to store user login information (username and password).
 * This object is instantiated in the LogInForm class.
 * @see LogInForm LoginForm
 */
public class LogInEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public LogInEvent(Object source) {
        super(source);
    }

    private String username;
    private char[] password;

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
}
