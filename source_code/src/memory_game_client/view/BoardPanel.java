package memory_game_client.view;

import memory_game_client.controller.Controller;
import memory_game_client.view.jobs.GameFinishedJob;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Stack;

/**
 * This panel contains all the visual components of the game board and cards.<br>
 * It also contains the logic for generating card pairs in single player mode (card pairs are generated
 * on the server side in multiplayer mode), checking whether opened pair is a match and updating the score
 * on the score board via Controller.
 */
public class BoardPanel extends JPanel implements ActionListener {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 750;
    private static final int NUM_OF_CARDS = 20;

    private Controller controller;
    private ActionListener actionListener;

    private JButton[] cards;
    private Icon defaultIcon;
    private Icon[] cardImages;

    private int[][] imagePairs;
    private int[] openedPair;

    private Timer timer;
    private int counter;

    private int player1Score;
    private int player2Score;
    private int totalScore;
    private boolean playerTurn;

    private int totalTries;
    private int performanceScore;

    private boolean isMultiPlayer;

    BoardPanel(boolean isMultiPlayer) {
        this.isMultiPlayer = isMultiPlayer;

        imagePairs = new int[NUM_OF_CARDS / 2][2];
        openedPair = new int[2];

        timer = new Timer(0, this);
        timer.setRepeats(false);
        timer.setInitialDelay(1500);

        counter = player1Score = player2Score = totalScore = totalTries = 0;

        playerTurn = true;

        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cardIndex = Integer.parseInt(e.getActionCommand());
                revealCard(cardIndex);
                removeActionListener(cardIndex);

                openedPair[counter % 2] = cardIndex;
                counter++;

                if (counter % 2 == 0) {

                    totalTries++;
                    controller.setAttempts(totalTries);
                    disableActionListener();

                    if (isMultiPlayer) {
                        int[] pairToSend = new int[2];
                        System.arraycopy(openedPair, 0, pairToSend, 0, openedPair.length);
                        controller.sendOpenedPair(pairToSend);
                        controller.opponentTurn();
                        playerTurn = false;
                    }

                    boolean pairFound = checkOpenedPair();

                    if (pairFound) {
                        incrementPlayerScore(1);
                        if (!isMultiPlayer) {
                            enableActionListener();
                        }
                    } else {
                        timer.start();      // executes actionPerformed() method after timer is triggered
                    }
                }
            }
        };

        initializeComps();
        initializeCards();
        setLayout();

        if (!isMultiPlayer) {
            generatePairs();
        }
    }

    private void initializeComps() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(225, 160, 115));
        this.setFocusable(true);

        Border raisedbevel, loweredbevel;
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();

        Border compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);

        this.setBorder(compound);
    }

    private void setLayout() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        for (JButton card : cards) {
            this.add(card);
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Initializes all the JButtons which represent the cards,
     * sets the default icons and adds ActionListeners to all of them.
     */
    private void initializeCards() {
        defaultIcon = new ImageIcon("icons/questionmark.jpg");
        Dimension dimension = new Dimension(160, 160);

        cards = new JButton[NUM_OF_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new JButton(defaultIcon);
            cards[i].setPreferredSize(dimension);
            cards[i].setActionCommand(Integer.toString(i));

            if (!isMultiPlayer) {
                cards[i].addActionListener(actionListener);
            }
        }

        cardImages = new Icon[NUM_OF_CARDS / 2];
        cardImages[0] = new ImageIcon("icons/bomb.png");
        cardImages[1] = new ImageIcon("icons/brown_mushroom.png");
        cardImages[2] = new ImageIcon("icons/coin.png");
        cardImages[3] = new ImageIcon("icons/green_mushroom.png");
        cardImages[4] = new ImageIcon("icons/luigi.png");
        cardImages[5] = new ImageIcon("icons/mario.png");
        cardImages[6] = new ImageIcon("icons/mask.png");
        cardImages[7] = new ImageIcon("icons/red_mushroom.png");
        cardImages[8] = new ImageIcon("icons/rocket.png");
        cardImages[9] = new ImageIcon("icons/turtle.png");
    }


    /**
     * Populates imagePairs array which holds the indexes of the card pairs with randomly generated unique numbers
     * in range of 0 (included) to NUM_OF_CARDS (excluded).
     */
    private void generatePairs() {
        Stack<Integer> indexes = new Stack<>();
        for (int i = 0; i < NUM_OF_CARDS; i++) {
            indexes.push(i);
        }
        Collections.shuffle(indexes);

        for (int[] pair : imagePairs) {
            pair[0] = indexes.pop();
            pair[1] = indexes.pop();
        }
    }

    public void setImagePairs(int[][] imagePairs) {
        this.imagePairs = imagePairs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hideCardPair(openedPair[0], openedPair[1]);

        if (playerTurn) {
            enableActionListener();
        }
    }

    /**
     * Disables JButtons on the board which are positioned on provided index numbers.
     * This is used when the card pair is found - it greys out the card pair.
     *
     * @param cardIndex1 index of the first JButton
     * @param cardIndex2 index of the second JButton
     */
    private void disableCardPair(int cardIndex1, int cardIndex2) {
        cards[cardIndex1].setEnabled(false);
        cards[cardIndex2].setEnabled(false);
    }

    /**
     * Restores the default questionmark.jpg icon for the provided JButton pair
     * which creates an impression that the card pair shown on those JButtons is being hidden.
     *
     * @param cardIndex1 index of the first JButton
     * @param cardIndex2 index of the second JButton
     */
    private void hideCardPair(int cardIndex1, int cardIndex2) {
        cards[cardIndex1].setIcon(defaultIcon);
        cards[cardIndex2].setIcon(defaultIcon);
    }

    /**
     * Changes the icon of the JButton at provided index
     * which creates an impression that the card is revealed.
     *
     * @param cardIndex index of the JButton
     */
    private void revealCard(int cardIndex) {
        cards[cardIndex].setIcon(cardImages[findCardIcon(cardIndex)]);
    }

    /**
     * Checks whether the pair of opened cards is a match by comparing indexes
     * stored inside openedPair array (which represent the opened pair)
     * with int pairs stored inside the imagePairs array (which holds the int pairs
     * of all the matching cards).<br>
     * If it's a match, disables the card pair.
     *
     * @return true if opened pair is a match, false otherwise
     */
    private boolean checkOpenedPair() {
        boolean pairFound = false;

        for (int i = 0; i < NUM_OF_CARDS / 2; i++) {
            if ((imagePairs[i][0] == openedPair[0] || imagePairs[i][0] == openedPair[1]) &
                    (imagePairs[i][1] == openedPair[0] || imagePairs[i][1] == openedPair[1])) {

                disableCardPair(openedPair[0], openedPair[1]);

                pairFound = true;
                break;
            }
        }

        return pairFound;
    }

    /**
     * Increments the score for the provided player and checks whether the game is over.<br>
     * If case the game is over, executes appropriate method depending on the game mode to
     * display the information about the outcome of the game on the score panel.
     *
     * @param player Player for whom to increment the score. 1 is always the client, 2 is the opponent.
     */
    private void incrementPlayerScore(int player) {

        if (controller != null) {
            if (player == 1) {
                player1Score += 10;
                controller.setPlayer1Score(player1Score);
            } else if (player == 2) {
                player2Score += 10;
                controller.setPlayer2Score(player2Score);
            }
            totalScore += 10;


            if (totalScore == 100) {

                if (!isMultiPlayer) {
                    performanceScore = (int) ((10f / totalTries) * 100);
                    new GameFinishedJob(controller, performanceScore).execute();

                } else {
                    int playerWon = 0;
                    if (player1Score > player2Score) {
                        playerWon = 1;
                    } else if (player2Score > player1Score) {
                        playerWon = 2;
                    }
                    controller.gameFinishedMultiplayer(playerWon);
                }
            }
        }
    }

    /**
     * Returns an icon index for the provided JButton index. The returned icon index is used
     * to fetch the icon from the cardImages Icon[] array.
     *
     * @param cardIndex index of the JButton
     * @return icon index for the provided JButton
     */
    private int findCardIcon(int cardIndex) {
        int iconIndex = 0;
        for (int i = 0; i < imagePairs.length; i++) {
            if (imagePairs[i][0] == cardIndex || imagePairs[i][1] == cardIndex) {
                iconIndex = i;
                break;
            }
        }
        return iconIndex;
    }

    /**
     * Removes the ActionListener for the JButton at provided index.
     *
     * @param cardIndex index of JButton on the board
     */
    private void removeActionListener(int cardIndex) {
        cards[cardIndex].removeActionListener(actionListener);
    }

    /**
     * Removes ActionListener from all JButtons.
     */
    private void disableActionListener() {
        for (JButton card : cards) {
            card.removeActionListener(actionListener);
        }
    }

    /**
     * Adds ActionListener to all JButtons.
     */
    private void enableActionListener() {
        for (JButton card : cards) {
            card.addActionListener(actionListener);
        }
    }

    public void playerTurn() {
        disableActionListener();
        enableActionListener();
        playerTurn = true;
    }

    public void opponentTurn() {
        disableActionListener();
        playerTurn = false;
    }

    /**
     * Used only in multiplayer mode.<br>
     * When an opponent opens a card pair, this method reveals the card pair
     * and checks whether an opened pair is a match.
     * If it's a match, increments opponent's score.
     * @param openedPair indexes of opened pair
     */
    public void setOpenedPair(int[] openedPair) {
        this.openedPair = openedPair;
        playerTurn = true;

        revealCard(openedPair[0]);
        revealCard(openedPair[1]);

        boolean pairFound = checkOpenedPair();
        if (pairFound) {
            incrementPlayerScore(2);
            enableActionListener();
        } else {
            timer.start();      // executes actionPerformed() method after timer is triggered
        }
    }

    public void refreshGame() {
        for (JButton card : cards) {
            card.setEnabled(true);
            card.setIcon(defaultIcon);
        }
        counter = player1Score = player2Score = totalScore = totalTries = 0;
    }

    /**
     * Processes the rematch code received.
     *<p>
     * -1, 1, 2 - rematchResponse - No <br>
     * 3 - rematchRequest <br>
     * </p>
     * */
    public int processRematchCode(int rematchCode) {
        int responseCode = -2;      // -2 doesn't represent anything

        switch (rematchCode) {
            case -1:
            case 1:
            case 2:
                JOptionPane.showMessageDialog(
                        null, "Your opponent declined your request!");
                break;
            case 3:
                responseCode = JOptionPane.showConfirmDialog(
                        null, "Your opponent wants a rematch. Accept?");
                break;
        }
        return responseCode;
    }
}
