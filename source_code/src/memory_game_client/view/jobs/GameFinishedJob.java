package memory_game_client.view.jobs;

import memory_game_client.controller.Controller;

/**
 * Displays the information about achieved result on the ScorePanel upon finishing single player game.<br>
 * Attempts to insert achieved result to the database.<br>
 * This job is executed on a new Thread in order to prevent the main thread getting blocked in case
 * database is not responding.
 */
public class GameFinishedJob implements Runnable {

    private final Controller controller;
    private final int performanceScore;

    public GameFinishedJob(Controller controller, int performanceScore) {
        this.controller = controller;
        this.performanceScore = performanceScore;
    }

    public void execute() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        controller.gameFinished(performanceScore);
    }
}
