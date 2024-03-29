package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.experts.Expert5;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertsFactory;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.model.gameField.IslandList;
import org.jetbrains.annotations.TestOnly;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

/**
 * Class Game, every class created has its own unique gameID
 *
 * @author Miglia
 */
public class Game implements Serializable {

    public final static int MAX_TILE = 12;
    private static final HashMap<Color, Pair<Player, Integer>> influenceMap = new HashMap<>(); //mapping the influence of every player
    public final int NUM_OF_PLAYERS;
    public final int MAX_NUM_OF_TOWERS;
    public final boolean EXP_MODE;
    public final int MAX_STUDENTS_ENTRANCE;
    //GAME PARAMETERS
    private final UUID gameID;
    private final ArrayList<TowerColor> AVAILABLE_TOWER_COLOR;
    private final ArrayList<DeckType> AVAILABLE_DECK_TYPE;
    private final ArrayList<Player> playersList;
    private final IslandList gameField;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private final PropertyChangeSupport support;
    //ONLY EXPERTS MODE
    private final ArrayList<ExpertCard> expertsCard = new ArrayList<>();
    public int coins = 20;
    private Color colorToIgnore = null;
    private Player playerHavingPlusTwo = null;       //player which have +2 influence if expert 8 is played


    private ExpertCard activeExpertCard;

    /**
     * Constructor, initializes the game parameters
     *
     * @param numberOfPlayers number of players in the match
     * @param expertMode      used to set the expert mode
     */
    public Game(int numberOfPlayers, boolean expertMode, int maxNumberOfTowers, int maxStudentEntrance, ArrayList<TowerColor> towerColorAvailable) {

        this.gameID = UUID.randomUUID();
        this.NUM_OF_PLAYERS = numberOfPlayers;
        this.pouch = new Pouch();
        this.gameField = new IslandList();
        this.MAX_NUM_OF_TOWERS = maxNumberOfTowers;
        this.MAX_STUDENTS_ENTRANCE = maxStudentEntrance;

        this.EXP_MODE = expertMode;
        //set to true if expert mode is selected

        this.AVAILABLE_DECK_TYPE = new ArrayList<>();
        Collections.addAll(AVAILABLE_DECK_TYPE, DeckType.DRUID, DeckType.KING, DeckType.SAGE, DeckType.WITCH);

        //creates cloudTiles
        this.cloudTiles = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            this.cloudTiles.add(new CloudTile(i));
        }

        //creates array of availableColor for the Players to choose
        this.playersList = new ArrayList<>();

        this.AVAILABLE_TOWER_COLOR = towerColorAvailable;

        //Initialization of influence map, at the beginning player with most influence is null
        for (Color color : Color.values()) {
            Pair<Player, Integer> internalPair = new Pair<>(null, 0);
            influenceMap.put(color, internalPair);
        }

        //ONLY IF EXPERT MODE IS SELECTED
        if (expertMode) {
            //Initial coin assignment

            //Expert cards drawing
            ExpertsFactory expertsDrawer = ExpertsFactory.createFactory(this);
            this.expertsCard.addAll(expertsDrawer.drawExperts());
        }

        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Method to add the player to the current game and automatically set the teammate
     *
     * @param nickname   nickname of the player
     * @param assistant  assistant deck chosen
     * @param towerColor tower color on the board chosen
     */
    public void addPlayer(String nickname, DeckType assistant, TowerColor towerColor) throws FileNotFoundException {
        if (AVAILABLE_TOWER_COLOR.remove(towerColor) && AVAILABLE_DECK_TYPE.remove(assistant))
            this.playersList.add(new Player(nickname, assistant, towerColor, this));
        else
            throw new FileNotFoundException("Color or assistant already taken");        //color already taken
        //Team formation
        if (NUM_OF_PLAYERS == 4 && playersList.size() == 4) {
            for (Player player : playersList) {
                for (Player playerTeamMate : playersList) {
                    if (playerTeamMate.getTowerColor().equals(player.getTowerColor())) {
                        player.getBoard().setTeamMate(playerTeamMate);
                        break;
                    }
                }
            }
        }
    }


    /**
     * Method to recharge the clouds tile at the beginning of every action phase
     */
    public void rechargeClouds() {

        try {

            for (int i = 0; i < NUM_OF_PLAYERS; i++) {

                if (NUM_OF_PLAYERS == 3)
                    cloudTiles.get(i).setStudents(this.pouch.randomDraw(4));
                else
                    cloudTiles.get(i).setStudents(this.pouch.randomDraw(3));

            }

            support.firePropertyChange("UpdateCloud", false, true);

        } catch (NotEnoughStudentsException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method used to check influence over inside hall of a player, it automatically set the teacher on the board if
     * needed
     *
     * @param activePlayer player who has changed the number of student
     * @param colorToCheck color to check the influence over
     */
    public void checkInfluence(Player activePlayer, Color colorToCheck) {
        int numOfCheckedStudent = activePlayer.getBoard().colorStudent(colorToCheck);

        //During an expert game, if the player has a teacher and he removes some students of that color
        //the method checks that he still has the max influence, either way the teacher is given to the player with max influence on that color
        if (this.EXP_MODE) {
            if (activePlayer.getBoard().getDiningRoom().get(colorToCheck) < influenceMap.get(colorToCheck).numOfStudents && activePlayer.getBoard().getTeacher(colorToCheck)) {
                activePlayer.getBoard().removeTeacher(colorToCheck);
                for (Player player : this.getPlayersList())
                    checkInfluence(player, colorToCheck);

            }
        }


        //if no one has ever played the color to check
        if (influenceMap.get(colorToCheck).getPlayer() == null && numOfCheckedStudent >= 1) {
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);
            activePlayer.getBoard().addTeachers(colorToCheck);
            support.firePropertyChange("UpdateTeacher", false, true);
        } else if (numOfCheckedStudent > influenceMap.get(colorToCheck).getNumOfStudents()) {

            //Add and remove the teacher from the involved players

            influenceMap.get(colorToCheck).getPlayer().getBoard().removeTeacher(colorToCheck);
            activePlayer.getBoard().addTeachers(colorToCheck);
            //Update influence map data
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);
            support.firePropertyChange("UpdateTeacher", false, true);
        } else
            support.firePropertyChange("UpdateBoard" + activePlayer.getNickname(), false, true);
    }

    /**
     * Method used to check influence on an island tile and setting the player with most influence on the island (if existing)
     *
     * @param nodeID island id to check the influence over
     */
    public void checkIslandInfluence(int nodeID) throws EndGameException {
        IslandNode islandToCheck = gameField.getIslandNode(nodeID);
        HashMap<Player, Integer> temporaryInfluenceCounter = new HashMap<>();  //temporary influence counter
        Player towerPlayer = null;
        //if island has a stop card on it influence haven't to be calculated
        if (!islandToCheck.isStopped()) {
            for (Color colorStudent : influenceMap.keySet()) {
                Player playerToCheck = influenceMap.get(colorStudent).getPlayer();
                //If player to check is null no one has still the influence on that color or the color is ignored
                if (playerToCheck != null) {
                    int influenceOfPlayer = islandToCheck.getColorInfluence(colorStudent);       //temp variable storing the number of student of the same color
                    //Check if there is a tower, if true add another point of influence
                    if (playerToCheck.getBoard().getTowerColor().equals(islandToCheck.getTowerColor())) {
                        if (!colorStudent.equals(colorToIgnore)) {
                            influenceOfPlayer = influenceOfPlayer + islandToCheck.getNumberOfTowers();
                        } else {
                            influenceOfPlayer = islandToCheck.getNumberOfTowers();
                        }
                        towerPlayer = playerToCheck;
                    } else {
                        if (colorStudent.equals(colorToIgnore))
                            influenceOfPlayer = 0;
                    }

                    if (temporaryInfluenceCounter.containsKey(playerToCheck)) {
                        temporaryInfluenceCounter.put(playerToCheck, temporaryInfluenceCounter.get(playerToCheck) + influenceOfPlayer);
                    } else {
                        temporaryInfluenceCounter.put(playerToCheck, influenceOfPlayer);
                    }
                }
            }
        } else {
            for (ExpertCard card : expertsCard) {
                if (card.getClass().getName().equals(Expert5.class.getName())) {
                    Expert5 stopCard = (Expert5) card;
                    stopCard.removeStop(islandToCheck);
                }
            }
        }
        //Setting plus two influence to player, only expert mode
        if (playerHavingPlusTwo != null) {

            Integer influenceToAdd = temporaryInfluenceCounter.get(playerHavingPlusTwo);
            if (influenceToAdd == null)
                influenceToAdd = 2;
            else
                influenceToAdd = influenceToAdd + 2;

            temporaryInfluenceCounter.put(playerHavingPlusTwo, influenceToAdd);
        }
        //removes from the hashmap all the players who have teachers but no influence on the island, so that the next isEmpty() works correctly
        temporaryInfluenceCounter.entrySet()
                .removeIf(
                        entry -> (entry.getValue() == 0));
        //If the temporary influence counter is empty no one has influence
        if (!temporaryInfluenceCounter.isEmpty()) {
            //check the player with most influence
            Player maxInfluencePlayer = null;
            for (Player player : temporaryInfluenceCounter.keySet()) {
                if (maxInfluencePlayer == null)
                    maxInfluencePlayer = player;
                if (temporaryInfluenceCounter.get(player) > temporaryInfluenceCounter.get(maxInfluencePlayer))
                    maxInfluencePlayer = player;
                else if (towerPlayer != null) {
                    if (temporaryInfluenceCounter.get(player).equals(temporaryInfluenceCounter.get(maxInfluencePlayer)) && towerPlayer.equals(player))
                        maxInfluencePlayer = player;
                }
            }

            if (!maxInfluencePlayer.getTowerColor().equals(islandToCheck.getTowerColor())) {
                //Set new most influence tower
                if(!islandToCheck.getTowerColor().equals(TowerColor.EMPTY))
                    islandToCheck.getMostInfluencePlayer().getBoard().addTower();
                //Set towers
                islandToCheck.setMostInfluencePlayer(maxInfluencePlayer);
                islandToCheck.setTower();
                gameField.checkTowersForMerge(nodeID);
            } else if (islandToCheck.getNumberOfTowers() == 0)
                islandToCheck.setTower();
        }
    }


    /**
     * Only expert mode, setter used for set the color to ignore in the influence calculus if Expert9 is played
     *
     * @param colorToIgnore color which I don't consider
     */
    public void setIgnoredColor(Color colorToIgnore) {
        this.colorToIgnore = colorToIgnore;
    }

    /**
     * Only expert mode, setter used to apply effect of expert8
     *
     * @param player who played the card
     */
    public void setPlayerHavingPlusTwo(Player player) {
        this.playerHavingPlusTwo = player;
    }


    /**
     * Getter of the influence map, an HashMap containing the color as key and a tuple of the student with that
     * influence and the number of student it has in his dining room
     */
    public HashMap<Color, Pair<it.polimi.ingsw.model.Player, java.lang.Integer>> getInfluenceMap() {
        return influenceMap;
    }

    /**
     * Method handling the coin in the expert mode, it transfers coins from the common reserve of the game to each player
     * personal reserve, passing a positive quantity will add coins to player taking them by the common reserve, passing
     * a negative quantity will do the vice-versa.
     *
     * @param player   to whom add/remove the money
     * @param quantity number of coin to transfer/remove
     */
    public void coinHandler(Player player, int quantity) {
        this.coins = this.coins - quantity;
        player.addCoin(quantity);
    }

    /**
     * Getter
     *
     * @return unique ID
     */
    public UUID getGameID() {
        return gameID;
    }


    //GETTER SECTION

    /**
     * Getter
     *
     * @return the GameField of the match
     */
    public IslandList getGameField() {
        return gameField;
    }

    /**
     * Getter
     *
     * @return The list of players in this match
     */
    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    /**
     * Getter
     * @return the pouch instance
     */
    public Pouch getPouch() {
        return pouch;
    }

    /**
     * Getter
     * @return all the cloud tiles
     */
    public ArrayList<CloudTile> getCloudTiles() {
        return cloudTiles;
    }

    /**
     * Getter
     * @param nickName is the nickname of the player required
     * @return the player whose nickname matches the required one
     */
    public Player getPlayerByNickName(String nickName) {
        for (Player currentPlayer : playersList) {
            if (currentPlayer.getNickname().equals(nickName))
                return currentPlayer;
        }
        throw new RuntimeException("Player Not Found");
    }

    /**
     * Getter
     * @return the number of coins available
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Getter
     * @return the expert cards available
     */
    public ArrayList<ExpertCard> getExpertsCard() {
        return expertsCard;
    }

    /**
     * Getter
     * @return the tower colors that are still available
     */
    public ArrayList<TowerColor> getAVAILABLE_TOWER_COLOR() {
        return AVAILABLE_TOWER_COLOR;
    }

    /**
     * Getter
     * @return the deck types that are still available
     */
    public ArrayList<DeckType> getAVAILABLE_DECK_TYPE() {
        return AVAILABLE_DECK_TYPE;
    }

    /**
     * Method used to set a propertyChangeListener on this instance of game
     * @param listener is the listener that will notify all the changes
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove a listener
     * @param listener listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Method to activate an expert card
     * @param activeExpertCard  is the expert card that has been activated
     */
    public void setActiveExpertsCard(ExpertCard activeExpertCard) {
        this.activeExpertCard = activeExpertCard;
    }

    /**
     * Getter
     * @return the expert card currently active
     */
    public ExpertCard getActiveExpertCard() {
        return activeExpertCard;
    }

    @TestOnly
    public void setExpertsCardTest(ArrayList<ExpertCard> testCard) {
        this.expertsCard.clear();
        this.expertsCard.addAll(testCard);
    }

    /**
     * Method used to create the gameFieldMap
     *
     * @return the gameField as a map
     */
    public Map<Integer, IslandNode> generateGameFieldMap() {
        Map<Integer, IslandNode> gameFieldMap = new HashMap<>();
        for (int i = 1; i <= getGameField().size(); i++) {
            gameFieldMap.put(i, getGameField().getIslandNode(i));
        }
        return gameFieldMap;
    }

    /**
     * Class used to define a new type of data for influence map(tuple)
     */
    public static class Pair<Player, Integer> {
        private Player player;
        private Integer numOfStudents;

        Pair(Player x, Integer y) {
            this.player = x;
            this.numOfStudents = y;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Integer getNumOfStudents() {
            return numOfStudents;
        }

        public void setNumOfStudents(Integer numOfStudents) {
            this.numOfStudents = numOfStudents;
        }
    }
}