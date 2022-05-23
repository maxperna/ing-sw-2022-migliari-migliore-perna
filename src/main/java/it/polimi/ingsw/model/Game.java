package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.gameField.IslandList;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.experts.Expert5;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertsFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class Game, every class created as its own unique gameID
 *
 * @author Miglia
 */
public class Game {

    public final static int MAX_TILE = 12;
    //GAME PARAMETERS
    private final UUID gameID;
    public final int NUM_OF_PLAYERS;
    public final int MAX_NUM_OF_TOWERS;
    public final boolean EXP_MODE;
    public final int MAX_STUDENTS_ENTRANCE;
    private final ArrayList<TowerColor> AVAILABLE_TOWER_COLOR;
    private final ArrayList<DeckType> AVAILABLE_DECK_TYPE;
    private final ArrayList<Player> playersList;
    private final IslandList gameField;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private static final HashMap<Color, Pair<Player, Integer>> influenceMap = new HashMap<>(); //mapping the influence of every player

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
     * @param expertMode used to set the expert mode
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
        Collections.addAll(AVAILABLE_DECK_TYPE,DeckType.DRUID,DeckType.KING,DeckType.SAGE,DeckType.WITCH);

        //creates cloudTiles
        this.cloudTiles = new ArrayList <>();
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
        if(expertMode) {
            //Initial coin assignment

            //Expert cards drawing
            ExpertsFactory expertsDrawer = ExpertsFactory.createFactory(this);
            this.expertsCard.addAll(expertsDrawer.drawExperts());
        }

        this.support = new PropertyChangeSupport(this);
    }

    /**Method to add the player to the current game and automatically set the team mate
     * @param nickname nickname of the player
     * @param assistant assistant deck chosen
     * @param towerColor tower color on the board chosen
     */
    public void addPlayer(String nickname,DeckType assistant, TowerColor towerColor) throws FileNotFoundException{
        if(AVAILABLE_TOWER_COLOR.remove(towerColor) && AVAILABLE_DECK_TYPE.remove(assistant))
            this.playersList.add(new Player(nickname,assistant,towerColor,this));
        else
            throw new FileNotFoundException("Color or assistant already taken");        //color already taken
        //Team formation
        if(NUM_OF_PLAYERS == 4 && playersList.size() == 4){
            for(Player player:playersList){
                for(Player playerTeamMate :playersList){
                    if(playerTeamMate.getTowerColor().equals(player.getTowerColor())) {
                        player.getBoard().setTeamMate(playerTeamMate);
                        break;
                    }
                }
            }
        }
    }


    /**Method to recharge the clouds tile at the beginning of every action phase
     * */
    public void rechargeClouds() {

        try {

            for(int i = 0; i < NUM_OF_PLAYERS; i++) {

                if (NUM_OF_PLAYERS == 3)
                    cloudTiles.get(i).setStudents(this.pouch.randomDraw(4));
                else
                    cloudTiles.get(i).setStudents(this.pouch.randomDraw(3));

            }

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
     * @return {@code true} if there is a change, {@code false} if not
     */
    public Boolean checkInfluence(Player activePlayer, Color colorToCheck) {
        int numOfCheckedStudent = activePlayer.getBoard().colorStudent(colorToCheck);
        boolean colorPlayed = true;      //set true if the color has been already played

        //if no one has ever played the color to check
        if (influenceMap.get(colorToCheck).getPlayer() == null) {
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);
            colorPlayed = false;
        }
        if (numOfCheckedStudent > influenceMap.get(colorToCheck).getNumOfStudents()) {

            //Add and remove the teacher from the involved players
            if(colorPlayed)
                influenceMap.get(colorToCheck).getPlayer().getBoard().removeTeacher(colorToCheck);
            activePlayer.getBoard().addTeachers(colorToCheck);
            //Update influence map data
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);
            return true;
        }
        return false;
    }

    /**
     * Method used to check influence on an island tile and setting the player with most influence on the island (if existing)
     *
     * @param nodeID island id to check the influence over
     */
    public void checkIslandInfluence(int nodeID) {
        Node islandToCheck = gameField.getIslandNode(nodeID);
        HashMap<Player, Integer> temporaryInfluenceCounter = new HashMap<>();  //temporary influence counter
        //if island has a deny card on it influence haven't to be calculated
        if(!islandToCheck.isStopped()) {
            for (Color colorStudent : islandToCheck.getStudents()) {
                Player playerToCheck = influenceMap.get(colorStudent).getPlayer();
                //If player to check is null no one ha still the influence on that color or the color is ignored
                if (playerToCheck != null && !colorStudent.equals(colorToIgnore)) {
                    int influenceOfPlayer = islandToCheck.getColorInfluence(colorStudent);       //temp variable storing the number of student of the same color
                    //Check if there is a tower, if true add another point of influence
                    if (playerToCheck.getBoard().getTowerColor().equals(islandToCheck.getTowerColor()))
                        influenceOfPlayer = influenceOfPlayer + islandToCheck.getNumberOfTowers();
                    if (temporaryInfluenceCounter.containsKey(playerToCheck)) {
                        temporaryInfluenceCounter.put(playerToCheck, influenceOfPlayer);
                    } else {
                        temporaryInfluenceCounter.put(playerToCheck, temporaryInfluenceCounter.get(playerToCheck) + influenceOfPlayer);
                    }
                }
            }
        }
        else{
            islandToCheck.removeStop();
            for(ExpertCard card:expertsCard){
                if (card.getClass().getName().equals("Expert5")){
                    Expert5 stopCard = (Expert5) card;
                    stopCard.removeStop(islandToCheck);
                }
            }
        }
        //Setting plus two influence to player, only expert mode
        if(playerHavingPlusTwo!=null){

            Integer influenceToAdd = temporaryInfluenceCounter.get(playerHavingPlusTwo);
            if(influenceToAdd == null)
                influenceToAdd = 2;
            else
                influenceToAdd = influenceToAdd +2;

            temporaryInfluenceCounter.put(playerHavingPlusTwo,influenceToAdd);
        }
        //If the temporary influence counter is empty no one has influence
        if (!temporaryInfluenceCounter.isEmpty()) {
            //check the player with most influence
            Player maxInfluencePlayer = temporaryInfluenceCounter.entrySet().stream().max((val1, val2) ->
                    val1.getValue() > val2.getValue() ? 1 : -1).get().getKey();

            islandToCheck.setMostInfluencePlayer(maxInfluencePlayer);
        }
    }


    /**Only expert mode, setter used for set the color to ignore in the influence calculus if Expert9 is played
     * @param colorToIgnore color which I don't consider*/
    public void setIgnoredColor(Color colorToIgnore){
        this.colorToIgnore = colorToIgnore;
    }

    /**Only expert mode, setter used to apply effect of expert8
     * @param player who played the card*/
    public void setPlayerHavingPlusTwo(Player player){
        this.playerHavingPlusTwo = player;
    }


    /**Getter of the influence map, an HashMap containing the color as key and a tuple of the student with that
     * influence and the number of student it has in his dining room*/
    public HashMap<Color, Pair<it.polimi.ingsw.model.Player, java.lang.Integer>> getInfluenceMap(){
        return influenceMap;
    }

    /**
     * Class used for define a new type of data for influence map(tuple)
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

    /**Method handling the coin in the expert mode, it transfers coins from the common reserve of the game to each player
     * personal reserve, passing a positive quantity will add coins to player taking them by the common reserve, passing
     * a negative quantity will do the vice-versa.
     * @param player to whom add/remove the money
     * @param quantity number of coin to transfer/remove */
    public void coinHandler(Player player,int quantity){
        this.coins = this.coins - quantity;
        player.addCoin(quantity);
    }


    //GETTER SECTION
    /**
     * Getter
     *
     * @return unique ID
     */
    public UUID getGameID() {
        return gameID;
    }

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


    public Pouch getPouch() {
        return pouch;
    }

    public ArrayList<CloudTile> getCloudTiles() {
        return cloudTiles;
    }


    public Player getPlayerByNickName(String nickName) {
        for (Player currentPlayer : playersList) {
            if(currentPlayer.getNickname().equals(nickName))
                return currentPlayer;
        }
        throw new RuntimeException("PlayerNonTrovato");
    }
    public int getCoins() {
        return coins;
    }

    public ArrayList<ExpertCard> getExpertsCard() {
        return expertsCard;
    }

    public ArrayList<TowerColor> getAVAILABLE_TOWER_COLOR() {
        return AVAILABLE_TOWER_COLOR;
    }

    public ArrayList<DeckType> getAVAILABLE_DECK_TYPE() {
        return AVAILABLE_DECK_TYPE;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**Method to activate an expert card*/
    public void setActiveExpertsCard(ExpertCard activeExpertCard){
        this.activeExpertCard = activeExpertCard;
    }

    public ExpertCard getActiveExpertCard() {
        return activeExpertCard;
    }
}