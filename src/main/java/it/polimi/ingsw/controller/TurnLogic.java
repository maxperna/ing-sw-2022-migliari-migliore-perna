package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.experts.Expert4;

import java.util.*;

/**
 * Class to handle all the logic inside a round such as the playing orders and to set the current active player
 *
 * @author Massimo
 */
public class TurnLogic {
    private final Game currentGame;   //game associated to round logic
    private final Map<Integer, Player> cardsPlayedActionNum;
    private final Map<Player, AssistantCard> playedCard;
    private final Queue<Player> playersOrders;  //Players order is a FIFO structure(both for playing orders and action phase)
    private Player lastRoundFirstPlayer;       //first player to play last round, used to define the starting point of the round
    private String currentPhase;

    /**
     * Default constructor
     * @param currentGame is the game that the TurnLogic will control
     */
    public TurnLogic(Game currentGame) {
        this.currentGame = currentGame;
        this.cardsPlayedActionNum = new HashMap<>();
        this.playedCard = new HashMap<>();
        this.playersOrders = new LinkedList<>();
        this.currentPhase = "PREPARATION";       //Always start in preparation phase
    }

    /**
     * Method to get the next current active player after the preparation phase or the action phase
     *
     * @return next active player or {@code null} if the queue is empty
     */
    public Player nextActivePlayer() {
        //Ending expert effect if change turn
        if (currentGame.getActiveExpertCard() != null)
            currentGame.getActiveExpertCard().endEffect();
        //Next player routine
        if (playersOrders.size() == currentGame.NUM_OF_PLAYERS) {
            this.lastRoundFirstPlayer = playersOrders.peek();
        }
        try {
            playersOrders.remove();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return getActivePlayer();
    }


    /**
     * Method to define round order in the action phase comparing played cards action number
     */
    private void defineActionPhaseOrders() {
        playersOrders.clear();
        //sorting the list from the highest action number to the lowest for a matter comfort
        List<Integer> roundCards = new ArrayList<>(cardsPlayedActionNum.keySet());
        Collections.sort(roundCards);

        for (Integer card : roundCards) {
            this.playersOrders.add(cardsPlayedActionNum.get(card));
        }

    }

    /**
     * Method to keep track of the played card during a round preparation phase, inserting by their action number
     *
     * @param actionNumber ID of the played card
     * @param player       player who used the card
     * @throws CardAlreadyPlayed if a player try to use a card already used by another player within the same round
     */
    public void setPlayedCard(int actionNumber, Player player) throws CardAlreadyPlayed, NonexistentCard, EndGameException {
        ArrayList<AssistantCard> assistantCards = player.getDeck().getRemainingCards();
        AssistantCard playedAssistantCard = null;
        for (AssistantCard card : assistantCards) {
            if (card.getActionNumber() == actionNumber)
                playedAssistantCard = card;
        }
        if (playedAssistantCard == null) {
            throw new NonexistentCard("You already played this card");
        }
        if (!this.cardsPlayedActionNum.containsKey(playedAssistantCard.getActionNumber())) {
            this.cardsPlayedActionNum.put(playedAssistantCard.getActionNumber(), player);
            player.playCard(playedAssistantCard);
            playedCard.put(player, playedAssistantCard);
        } else throw new CardAlreadyPlayed("Another player already used this card");


    }

    /**
     * Method to set a random players order during the first round of the game or an order based on the last first player
     * on the previous round( always in anticlockwise sense)
     */
    public void generatePreparationPhaseOrder() {
        //branch to generate a casual order, if lastRoundFirstPlayer is null no round has been already played
        if (lastRoundFirstPlayer == null) {
            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(currentGame.NUM_OF_PLAYERS);
            //Loading players orders in anticlockwise sense starting by a random number
            while (this.playersOrders.size() != currentGame.NUM_OF_PLAYERS) {
                this.playersOrders.add(currentGame.getPlayersList().get(randomNumber));
                randomNumber = (randomNumber + 1) % currentGame.NUM_OF_PLAYERS;
            }
        }
        //branch to generate playing order during game based on last round first player
        else {
            int index = currentGame.getPlayersList().indexOf(lastRoundFirstPlayer);
            while (this.playersOrders.size() != currentGame.NUM_OF_PLAYERS) {
                this.playersOrders.add(currentGame.getPlayersList().get(index));
                index = (index + 1) % currentGame.NUM_OF_PLAYERS;
            }
        }

    }

    /**
     * Method to switch the current phase (from preparation to action or vice-versa)
     */
    public void switchPhase() {
        if (currentPhase.equals("PREPARATION")) {
            currentPhase = "ACTION";
            defineActionPhaseOrders();       //automatically start the action phase
        } else {
            endTurn();
            currentPhase = "PREPARATION";
            generatePreparationPhaseOrder();
        }
    }

    /**
     * Method to end current turn and start a new one
     */
    private void endTurn() {
        playersOrders.clear();
        cardsPlayedActionNum.clear();
        playedCard.clear();
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public Player getActivePlayer() {
        return playersOrders.peek();
    }

    public Queue<Player> getPlayersOrders() {
        return playersOrders;
    }

    public Map<Integer, Player> getCardsPlayed() {
        return cardsPlayedActionNum;
    }

    //ACTION PHASE PART

    /**
     * Method used to move a student from the entryHall to the diningRoom of a given board
     * @param player is the player that performs the action
     * @param student is the student that will be moved
     */
    public void moveStudentOnBoard(Player player, Color student) {
        try {
            player.getBoard().moveEntryToDiningRoom(student);
        } catch (NotOnBoardException | NotEnoughSpace e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to move mother nature over the island list
     * @param numOfSteps is the number of steps that mother nature will perform
     * @param player is the player who actually controls mother nature
     * @throws IllegalMove when the number of steps is 0 or less, or when it's higher than the allowed number
     */
    public void moveMotherNature(Player player, int numOfSteps) throws IllegalMove, EndGameException {
        int allowedNumOfSteps = playedCard.get(player).getMotherNatureControl();
        //IF expert4 is played modify allowed num of steps
        if (currentGame.EXP_MODE) {
            if (currentGame.getActiveExpertCard() != null)
                if (currentGame.getActiveExpertCard().getClass().equals(Expert4.class))
                    allowedNumOfSteps = allowedNumOfSteps + 2;
        }
        if (numOfSteps > allowedNumOfSteps || numOfSteps == 0)
            throw new IllegalMove("Too much steps");
        else {
            currentGame.getGameField().moveMotherNatureWithGivenMoves(numOfSteps);
            int nodeIDMN = currentGame.getGameField().getMotherNature().getNodeID();
            currentGame.checkIslandInfluence(nodeIDMN);
        }

    }


    public void chooseCloudTile(Player player, int cloudID) {
        try {
            ArrayList<Color> pickedStudents = new ArrayList<>(currentGame.getCloudTiles().get(cloudID).getStudents());
            player.getBoard().addStudentsEntryRoom(pickedStudents);
        } catch (NotEnoughSpace e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to play an expert card that requires no additional parameters
     * @param playedCard is  the ID [0,1,2] that refers to the card played
     * @param player is the player who activated the expert effect
     * @throws NotEnoughCoins when the player has less than the required coins to activate the effect
     **/
    public void playExpertCard(Player player, int playedCard) throws NotEnoughCoins {
        currentGame.getExpertsCard().get(playedCard).useCard(player);
    }

    /**
     * Method to play an expert card that requires a node ID as parameter
     * @param playedCard is the ID [0,1,2] that refers to the card played
     * @param nodeId is the ID of the chosen island
     * @param player is the player who activated the expert effect
     * @throws NotEnoughCoins when the player has less than the required coins to activate the effect
     * @throws IllegalMove when the nodeId is not inside the islandList
     **/
    public void playExpertCard(Player player, int nodeId, int playedCard) throws IllegalMove, NotEnoughCoins {
        currentGame.getExpertsCard().get(playedCard).useCard(player, nodeId);
    }

    /**
     * Method to play an expert card that requires a node ID and a Color as parameters
     * @param playedCard is the ID [0,1,2] that refers to the card played
     * @param nodeId is the ID of the chosen island
     * @param studentColor is the color chosen (can also refer to a student of that color, based on which expert has been played)
     * @param player is the player who activated the expert effect
     * @throws IllegalMove when the student is not available, or when the island is not available
     * @throws NotEnoughCoins when the player has less than the required coins to activate the effect
     **/
    public void playExpertCard(Player player, int nodeId, Color studentColor, int playedCard) throws IllegalMove, NotEnoughCoins {
        currentGame.getExpertsCard().get(playedCard).useCard(player, nodeId, studentColor);
    }

    /**
     * Method to play an expert card that requires a color as parameter
     * @param playedCard is the ID [0,1,2] that refers to the card played
     * @param student is the color chosen (can also refer to a student of that color, based on which expert has been played)
     * @throws NotEnoughCoins when the player has less than the required coins to activate the effect
     * @throws IllegalMove when the Color passed is not available on the card
     * @throws NotOnBoardException when the Color passed is not available on player's board
     **/
    public void playExpertCard(Player player, Color student, int playedCard) throws IllegalMove, NotEnoughCoins, NotOnBoardException {
        currentGame.getExpertsCard().get(playedCard).useCard(player, student);
    }

    /**
     * Method to play an expert card that requires two arrayLists of Color as parameters
     * @param playedCard is the ID [0,1,2] that refers to the card played
     * @param studentSet1 is the first arrayList of students, usually the ones that will be taken from an external source
     * @param studentSet2 is the second arrayList of students, usually tha ones that will be moved to an external source
     * @throws NotEnoughCoins when the player has less than the required coins to activate the effect
     * @throws IllegalMove when the player has less than the required coins to activate the effect
     * @throws NotOnBoardException when the Color passed is not available on player's board
     **/
    public void playExpertCard(Player player, ArrayList<Color> studentSet1, ArrayList<Color> studentSet2, int playedCard) throws IllegalMove, NotEnoughCoins, NotOnBoardException {
        currentGame.getExpertsCard().get(playedCard).useCard(player, studentSet1, studentSet2);
    }

}










