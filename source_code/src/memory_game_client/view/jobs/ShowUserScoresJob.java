package memory_game_client.view.jobs;

import memory_game_client.controller.Controller;
import memory_game_client.view.MyScoresFrame;

import java.util.HashMap;

/**
 * Opens a new JFrame and displays all saved scores for the current user.
 * <p>
 * This job is executed on a new Thread in order to prevent the main thread getting blocked in case
 * database is not responding. Also, since user scores window is opened in a JFrame, MainFrame window which
 * is running on a main Thread is not blocked by opening user scores window.
 * </p>
 */
public class ShowUserScoresJob implements Runnable {

    private final Controller controller;

    public ShowUserScoresJob(Controller controller) {
        this.controller = controller;
    }

    public void execute() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        HashMap<String, Integer> scores = new HashMap<>();
        controller.getScoresForUser(scores);
        new MyScoresFrame(scores);
    }
}
