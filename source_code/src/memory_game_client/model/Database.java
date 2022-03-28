package memory_game_client.model;

import java.sql.*;
import java.util.HashMap;

/**
 * Contains methods for connecting/disconnecting and executing CRUD operations on MySQL Database.
 */
public class Database {

    private Connection connection;

    private boolean connect() {
        boolean isConnected = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://db4free.net:3306/twitter_db";
            String username = "ddino90";
            String password = "dd1n01990";

            connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected: " + url);
            isConnected = true;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return isConnected;
    }

    private void disconnect() {
        try {
            connection.close();
//            System.out.println("Disconnected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends user registration information to database for further processing.
     * @return Database response.<br>
     * Code -1 is returned when connection to database could not be established<br>
     * Code 0 is returned when username is already taken<br>
     * Code 1 is returned when registration is successful
     */
    public int userRegistration(String username, String password) {
        boolean connectionEstablished = connect();
        username = username.strip();
        password = password.strip();

        if (connectionEstablished) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM users WHERE BINARY username = ?");

                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    disconnect();
                    return 0;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO users (username, password) VALUES (?, ?)");

                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            disconnect();
            return 1;

        } else {
            return -1;
        }
    }

    /**
     * Sends user login information to database for further processing.
     * @return Database response.<br>
     * Code -1 is returned when connection to database could not be established<br>
     * Code 0 is returned when username or password is wrong<br>
     * Code 1 is returned when login is successful
     */
    public int userLogin(String username, String password) {
        boolean connectionEstablished = connect();
        username = username.strip();
        password = password.strip();

        if (connectionEstablished) {

            try {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM users WHERE BINARY username = ? AND BINARY password = ?");

                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    disconnect();
                    return 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            disconnect();
            return 0;

        } else {
            return -1;
        }
    }

    /**
     * Sends a Performance score to the database to be inserted.
     */
    public void insertScore(String playerName, int performanceScore) {
        boolean connectionEstablished = connect();

        if (connectionEstablished) {
            int userId = getUserId(playerName);

            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO user_scores (user_id, score) VALUES (?, ?)");

                statement.setInt(1, userId);
                statement.setInt(2, performanceScore);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            checkHighScore(userId, performanceScore);

            disconnect();
        }
    }

    /**
     * Retrieves user's id from the database based on the username given.
     * @return user's id
     */
    private int getUserId(String username) {
        int userId;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE BINARY username = ?");

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
                return userId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Checks if the score provided is higher than the current high score for the user.
     * @param userId id of the user
     * @param performanceScore score to be compared to current high score
     */
    private void checkHighScore(int userId, int performanceScore) {
        int currentHighScore = getHighScore(userId);

        if (performanceScore > currentHighScore) {
            insertHighScore(userId, performanceScore);
        }
    }

    private void insertHighScore(int userId, int highScore) {

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user_highscores WHERE user_id = ?");

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                statement = connection.prepareStatement(
                        "UPDATE user_highscores SET highscore = ? WHERE user_id = ?");

                statement.setInt(1, highScore);
                statement.setInt(2, userId);
                statement.executeUpdate();

            } else {
                statement = connection.prepareStatement(
                        "INSERT INTO user_highscores (user_id, highscore) VALUES (?, ?)");

                statement.setInt(1, userId);
                statement.setInt(2, highScore);
                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns current high score for the user provided.
     * @param userId id of the user to retrieve high score for
     * @return current high score for the user
     */
    private int getHighScore(int userId) {
        int currentHighScore = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user_highscores WHERE user_id = ?");

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                currentHighScore = resultSet.getInt("highscore");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currentHighScore;
    }


    /**
     * Stores all the scores for the provided user in the provided storage.
     * <p>
     *     Date and time are stored as a String while score is stored as an Integer.
     * </p>
     * @param username user's username
     * @param storage HashMap in which scores all stored
     */
    public void getScoresForUser(String username, HashMap<String, Integer> storage) {

        connect();

        int userId = getUserId(username);

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user_scores WHERE user_id = ?");

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            int score;
            String dateTime;
            while (resultSet.next()) {
                score = resultSet.getInt("score");
                dateTime = resultSet.getString("datetime_scored");
                storage.put(dateTime, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }


    /**
     * Stores the high score of all the users inside the storage provided.
     * <p>
     *     User names are stored as a String and scores are stored as an Integer.
     * </p>
     * @param storage storage to store the high scores to
     */
    public void getHighScores(HashMap<String, Integer> storage) {

        connect();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.username, user_highscores.highscore FROM user_highscores " +
                            "LEFT JOIN users ON user_highscores.user_id = users.id");
            ResultSet resultSet = statement.executeQuery();

            int score;
            String username;
            while (resultSet.next()) {
                score = resultSet.getInt("user_highscores.highscore");
                username = resultSet.getString("users.username");
                storage.put(username, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }
}
