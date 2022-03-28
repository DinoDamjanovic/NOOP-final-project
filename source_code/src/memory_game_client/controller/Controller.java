package memory_game_client.controller;

import memory_game_client.model.Client;
import memory_game_client.model.Database;
import memory_game_client.view.BoardPanel;
import memory_game_client.view.ScorePanel;
import memory_game_client.view.logInRegistration.LogInEvent;
import memory_game_client.view.logInRegistration.RegistrationEvent;

import java.util.HashMap;

/**
 * Controller for the client application.
 * <p>
 *     Executes CRUD operations on MySQL Database.<br>
 *     In addition, acts as bridge between board panel and score panel.
 * </p>
 */
public class Controller {

    private Client client;
    private ScorePanel scorePanel;
    private BoardPanel boardPanel;
    private Database database;
    private String username;

    public Controller() {
        this.database = new Database();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setScorePanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlayer1Score(int score) {
        if (scorePanel != null) {
            scorePanel.setPlayer1Score(score);
        }
    }

    public void setPlayer2Score(int score) {
        if (scorePanel != null) {
            scorePanel.setPlayer2Score(score);
        }
    }

    public void setPlayer2Name(String playerName) {
        scorePanel.setPlayer2Name(playerName);
    }

    public void setImagePairs(int[][] imagePairs) {
        boardPanel.setImagePairs(imagePairs);
    }

    /**
     * Sends user registration information to database for further processing.
     * @param registrationEvent object which holds information about the user
     * @return Database response.<br>
     * Code -1 is returned when connection to database could not be established<br>
     * Code 0 is returned when username is already taken<br>
     * Code 1 is returned when registration is successful
     */
    public int userRegistration(RegistrationEvent registrationEvent) {
        String username = registrationEvent.getUsername();
        String password = String.valueOf(registrationEvent.getPassword());
        return database.userRegistration(username, password);
    }

    /**
     * Sends user login information to database for further processing.
     * @param logInEvent object which holds information about the user
     * @return Database response.<br>
     * Code -1 is returned when connection to database could not be established<br>
     * Code 0 is returned when username or password is wrong<br>
     * Code 1 is returned when login is successful
     */
    public int userLogin(LogInEvent logInEvent) {
        String username = logInEvent.getUsername();
        String password = String.valueOf(logInEvent.getPassword());
        return database.userLogin(username, password);
    }

    /**
     * Passes on HashMap to the database. Database then fills the HashMap
     * with user's scores along with date and time each score was achieved.
     * @param storage HashMap in which user's scores are stored
     */
    public void getScoresForUser(HashMap<String, Integer> storage) {
        database.getScoresForUser(username, storage);
    }

    /**
     * Displays the information about achieved result on the ScorePanel upon finishing single player game.<br>
     * Attempts to insert achieved result to the database.
     * @param performanceScore achieved result to be displayed and inserted
     */
    public void gameFinished(int performanceScore) {
        scorePanel.setGameFinishedText();
        scorePanel.setPlayer1PerformanceScore(performanceScore);
        database.insertScore(username, performanceScore);
    }

    /**
     * Displays information about whom won the multiplayer game on the ScorePanel.
     * @param player A player that won the game.<br>
     *               Value 1 represents the client. Value 2 represents the opponent.
     */
    public void gameFinishedMultiplayer(int player) {
        scorePanel.setGameFinishedText(player);
    }

    /**
     * Fetches High scores of all the users from the database in String, Integer pairs
     * where String represents the name of the player and Integer is the score.
     * @param storage HashMap used to store the results
     */
    public void getHighScores(HashMap<String, Integer> storage) {
        database.getHighScores(storage);
    }

    /**
     * Sends a pair of int values (which represent the opened card pair) stored inside an int[] array
     * @param openedPair int pair to send
     */
    public void sendOpenedPair(int[] openedPair) {
        client.sendOpenedPair(openedPair);
    }

    /**
     * Sets the provided pair of cards represented as a pair of ints as a currently opened pair.
     * @param openedPair int pair to set
     */
    public void setOpenedPair(int[] openedPair) {
        boardPanel.setOpenedPair(openedPair);
    }

    public void playerTurn() {
        scorePanel.playerTurn();
    }

    public void opponentTurn() {
        scorePanel.opponentTurn();
    }

    /**
     * Enables all the disabled cards on the board, hides them and refreshes the score(s).
     */
    public void refreshGame() {
        boardPanel.refreshGame();
        scorePanel.refreshGame();
    }

    /**
     * Sends a rematch request code to the opponent.
     */
    public void sendRematchRequest() {
        client.sendRematchRequest();
    }

    public int processRematchCode(int rematchCode) {
        return boardPanel.processRematchCode(rematchCode);
    }

    public void processTurnToPlay(Boolean turnToPlay) {
        if (turnToPlay) {
            scorePanel.playerTurn();
            boardPanel.playerTurn();
        } else {
            scorePanel.opponentTurn();
            boardPanel.opponentTurn();
        }
    }

    public void setAttempts(int attempts) {
        scorePanel.setAttempts(attempts);
    }
}
