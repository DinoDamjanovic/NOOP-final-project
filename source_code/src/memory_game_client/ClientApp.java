package memory_game_client;

import memory_game_client.view.logInRegistration.LogInRegistrationFrame;

import javax.swing.*;

public class ClientApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LogInRegistrationFrame();
            }
        });
    }
}
