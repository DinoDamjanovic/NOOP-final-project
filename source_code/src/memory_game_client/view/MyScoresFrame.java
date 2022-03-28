package memory_game_client.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Displays scores of the current user.
 */
public class MyScoresFrame extends JFrame {

    private static final int WIDTH = 400;

    private JTextArea scoreboard;
    private JScrollPane scrollPane;

    private JPanel infoPanel;
    private JPanel commandsPanel;

    private JLabel scoreLabel;
    private JLabel timeLabel;

    private JButton sortByScoreAsc;
    private JButton sortByScoreDesc;
    private JButton sortByTimeAsc;
    private JButton sortByTimeDesc;

    private HashMap<String, Integer> scores;
    private TreeMap<LocalDateTime, Integer> sortedScores;

    public MyScoresFrame(HashMap<String, Integer> scores) {
        this.scores = scores;
        this.sortedScores = new TreeMap<>();

        this.setTitle("My Scores");
        ImageIcon image = new ImageIcon("external_libs/icons/frameIcon.png");
        setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setLayout();

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        sortEntriesIntoTreeMap();
    }

    private void initializeComponents() {
        Font font = new Font("Calibri", Font.BOLD, 20);
        Color colorBlue = new Color(235, 247, 255);
        Color colorWhite = new Color(255, 255, 255);
        Dimension buttonSize = new Dimension(150, 40);

        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 15));
        infoPanel.setPreferredSize(new Dimension(WIDTH, 40));
        infoPanel.setBackground(colorBlue);

        scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(font);
        timeLabel = new JLabel("Date & Time:");
        timeLabel.setFont(font);

        infoPanel.add(scoreLabel);
        infoPanel.add(timeLabel);

        scoreboard = new JTextArea();

        scrollPane = new JScrollPane(
                scoreboard, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(WIDTH, 400));

        commandsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        commandsPanel.setPreferredSize(new Dimension(WIDTH, 150));
        commandsPanel.setBackground(colorBlue);

        Border border, titledBorder;
        border = BorderFactory.createEmptyBorder();
        titledBorder = BorderFactory.createTitledBorder("Sort by");

        Border compound = BorderFactory.createCompoundBorder(
                border, titledBorder);

        commandsPanel.setBorder(compound);

        sortByScoreAsc = new JButton("Score Asc");
        sortByScoreAsc.setBackground(colorWhite);
        sortByScoreAsc.setFont(font);
        sortByScoreAsc.setPreferredSize(buttonSize);
        sortByScoreAsc.setVerticalAlignment(SwingConstants.BOTTOM);
        sortByScoreAsc.setActionCommand("ScoreAsc");

        sortByScoreDesc = new JButton("Score Desc");
        sortByScoreDesc.setBackground(colorWhite);
        sortByScoreDesc.setFont(font);
        sortByScoreDesc.setPreferredSize(buttonSize);
        sortByScoreDesc.setVerticalAlignment(SwingConstants.BOTTOM);
        sortByScoreDesc.setActionCommand("ScoreDesc");

        sortByTimeAsc = new JButton("Date Asc");
        sortByTimeAsc.setBackground(colorWhite);
        sortByTimeAsc.setFont(font);
        sortByTimeAsc.setPreferredSize(buttonSize);
        sortByTimeAsc.setVerticalAlignment(SwingConstants.BOTTOM);
        sortByTimeAsc.setActionCommand("DateAsc");


        sortByTimeDesc = new JButton("Date Desc");
        sortByTimeDesc.setBackground(colorWhite);
        sortByTimeDesc.setFont(font);
        sortByTimeDesc.setPreferredSize(buttonSize);
        sortByTimeDesc.setActionCommand("DateDesc");

        commandsPanel.add(sortByScoreAsc);
        commandsPanel.add(sortByScoreDesc);
        commandsPanel.add(sortByTimeAsc);
        commandsPanel.add(sortByTimeDesc);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "ScoreAsc":
                        sortByScore("Asc");
                        break;
                    case "ScoreDesc":
                        sortByScore("Desc");
                        break;
                    case "DateAsc":
                        sortByDate("Asc");
                        break;
                    case "DateDesc":
                        sortByDate("Desc");
                        break;
                }
            }
        };
        sortByScoreAsc.addActionListener(actionListener);
        sortByScoreDesc.addActionListener(actionListener);
        sortByTimeAsc.addActionListener(actionListener);
        sortByTimeDesc.addActionListener(actionListener);
    }

    private void setLayout() {
        this.setLayout(new BorderLayout());
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(commandsPanel, BorderLayout.SOUTH);
    }

    /**
     * Sorts entries into TreeMap where Key is LocalDateTime and Value is Integer
     * then prints the TreeMap in descending order.
     */
    private void sortEntriesIntoTreeMap() {
        LocalDateTime dateTime;
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Map.Entry<String, Integer> dateScoreEntry : scores.entrySet()) {
            dateTime = LocalDateTime.parse(dateScoreEntry.getKey(), inputFormat);
            sortedScores.put(dateTime, dateScoreEntry.getValue());
        }

        sortByDate("Desc");
    }

    /**
     * Retrieves sorted KeySet from the sortedScores TreeMap
     * where Keys are LocalDateTime objects then prints the KeySet.
     * @param direction Determines whether Keys are sorted in ascending or descending order.
     */
    private void sortByDate(String direction) {
        NavigableSet<LocalDateTime> sortedSet = null;

        switch (direction) {
            case "Asc":
                sortedSet = sortedScores.navigableKeySet();
                break;
            case "Desc":
                sortedSet = sortedScores.descendingKeySet();
                break;
        }

        printSortedSet(sortedSet);
    }

    /**
     * Sorts the entries from the TreeMap<LocalDateTime, Integer> by Values, where Values are scores.
     * @param direction Determines whether Values are sorted in ascending or descending order.
     */
    private void sortByScore(String direction) {

        SortedSet<Map.Entry<LocalDateTime, Integer>> sortedEntries = new TreeSet<>(
                new Comparator<>() {
                    @Override
                    public int compare(Map.Entry<LocalDateTime, Integer> e1, Map.Entry<LocalDateTime, Integer> e2) {
                        int res = 0;
                        switch (direction) {
                            case "Asc":
                                res = e1.getValue().compareTo(e2.getValue());
                                break;
                            case "Desc":
                                res = e2.getValue().compareTo(e1.getValue());
                                break;
                        }
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(sortedScores.entrySet());

        printSortedSet(sortedEntries);
    }

    /**
     * Clears the text area and prints the provided sortedSet.
     * @param sortedSet NavigableSet to be printed
     */
    private void printSortedSet(NavigableSet<LocalDateTime> sortedSet) {
        scoreboard.setText("");

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        for (LocalDateTime scoreEntry : sortedSet) {
            scoreboard.append("\t" + sortedScores.get(scoreEntry) + "\t              " +
                    scoreEntry.format(outputFormat) + "\n");
        }
    }

    /**
     * Clears the text area and prints the provided sortedSet.
     * @param sortedSet SortedSet to be printed
     */
    private void printSortedSet(SortedSet<Map.Entry<LocalDateTime, Integer>> sortedSet) {
        scoreboard.setText("");

        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        for (Map.Entry<LocalDateTime, Integer> scoreEntry : sortedSet) {
            scoreboard.append("\t" + scoreEntry.getValue() + "\t              " +
                    scoreEntry.getKey().format(outputFormat) + "\n");
        }
    }
}
