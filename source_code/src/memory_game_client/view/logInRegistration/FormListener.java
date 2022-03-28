package memory_game_client.view.logInRegistration;

import memory_game_client.controller.Controller;

/**
 * Used by LogInRegistrationFrame class in order to pass user login and user registration info to the Controller.
 *
 * @see LogInRegistrationFrame LogInRegistrationFrame
 */
public interface FormListener {

    /**
     * Passes on LogInEvent object to the Controller. If Controller returns information that
     * login was successful, it opens the Game Mode window and passes
     * the user's username to it, otherwise it displays appropriate warning or error message to the user.
     *
     * @param logInEvent contains user data - username and password
     * @see Controller
     */
    void logInEventOccured(LogInEvent logInEvent);

    /**
     * First checks if entered password and confirmation password match - if not, informs the user.<br>
     * If passwords match, passes on RegistrationEvent object to the Controller then receives information
     * from the Controller whether registration was successful or not and informs the user about it.
     *
     * @param registrationEvent contains user data - username, password and password confirmation
     * @see Controller
     */
    void registrationEventOccured(RegistrationEvent registrationEvent);
}
