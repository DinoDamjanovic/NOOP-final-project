package memory_game_client.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Displays high scores of all the users.
 */
public class HighScoresFrame extends JFrame {

    private static final int WIDTH = 300;

    private JTextArea scoreboard;
    private JScrollPane scrollPane;

    private JPanel infoPanel;

    private JLabel scoreLabel;
    private JLabel nameLabel;

    private HashMap<String, Integer> scores;

    public HighScoresFrame(HashMap<String, Integer> scores) {
        this.scores = scores;
        this.setTitle("High Scores");
        ImageIcon image = new ImageIcon("external_libs/icons/frameIcon.png");
        setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setLayout();

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        sortByScore();
    }

    private void initializeComponents() {
        Font font = new Font("Calibri", Font.BOLD, 20);
        Color colorBlue = new Color(235, 247, 255);

        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        infoPanel.setPreferredSize(new Dimension(WIDTH, 40));
        infoPanel.setBackground(colorBlue);

        scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(font);
        nameLabel = new JLabel("Player:");
        nameLabel.setFont(font);

        infoPanel.add(scoreLabel);
        infoPanel.add(nameLabel);

        scoreboard = new JTextArea();

        scrollPane = new JScrollPane(
                scoreboard, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(WIDTH, 400));
    }

    private void setLayout() {
        this.setLayout(new BorderLayout());
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Initializes a new TreeSet which sorts the score entries from the scores HashMap by value.
     * Once sorted by value, entries are printed to text area.
     */
    private void sortByScore() {

        SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<>(
                new Comparator<>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(scores.entrySet());

        printScores(sortedEntries);
    }

    private void printScores(SortedSet<Map.Entry<String, Integer>> sortedSet) {
        scoreboard.setText("");

        for (Map.Entry<String, Integer> scoreEntry : sortedSet) {
            scoreboard.append("\t" + scoreEntry.getValue() + "\t" + scoreEntry.getKey() + "\n");
        }
    }
}
