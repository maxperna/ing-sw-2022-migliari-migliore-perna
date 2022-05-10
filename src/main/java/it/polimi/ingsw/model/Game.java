package it.polimi.ingsw.model;

import it.polimi.ingsw.gameField.IslandList;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertsFactory;

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
    public final boolean EXPERT_MODE;       //set to true if expert mode is selected
    public final int NUM_OF_PLAYERS;
    public final int MAX_NUM_OF_TOWERS;
    public final int MAX_STUDENTS_ENTRANCE;
    private final ArrayList<TowerColor> TOWER_COLORS_AVAILABLE;
    private final UUID gameID;
    private final ArrayList<Player> playersList;

    private final IslandList gameField;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private static final HashMap<Color, Pair<Player, Integer>> influenceMap = new HashMap<>(); //mapping the influence of every player

    //ONLY EXPERTS MODE
    private final ArrayList<ExpertCard> expertsCard = new ArrayList<>();
    public int coins = 20;

    private Color colorToIgnore = null;
    private Player playerHavingPlusTwo = null;       //player which have +2 influence if expert 8 is played

    /**
     * Constructor, initializes the game parameters
     *
     * @param numberOfPlayers number of players in the match
     * @param expertMode used to set the expert mode
     */
    public Game(int numberOfPlayers, boolean expertMode) {

        this.gameID = UUID.randomUUID();
        this.NUM_OF_PLAYERS = numberOfPlayers;
        this.pouch = new Pouch();
        this.gameField = new IslandList();
        this.EXPERT_MODE = expertMode;

        //creates cloudTiles
        this.cloudTiles = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            this.cloudTiles.add(new CloudTile(i));
        }

        this.playersList = new ArrayList<>();
        List<TowerColor> towerColors = new ArrayList<>(Arrays.asList(TowerColor.BLACK,TowerColor.WHITE));
        switch (numberOfPlayers) {
            case 2:

            case 4: {


                this.MAX_NUM_OF_TOWERS = 8;
                this.MAX_STUDENTS_ENTRANCE = 7;

                break;
            }

            case 3: {
                this.MAX_NUM_OF_TOWERS = 6;
                this.MAX_STUDENTS_ENTRANCE = 9;
                towerColors.add(TowerColor.GRAY);


                break;
            }

            default:
                throw new IllegalArgumentException("Unknown number of players");
        }
        this.TOWER_COLORS_AVAILABLE = new ArrayList<>(towerColors);

        //Initialization of influence map, at the beginning player with most influence is null
        for (Color color : Color.values()) {
            Pair<Player, Integer> internalPair = new Pair<>(null, 0);
            influenceMap.put(color, internalPair);
        }

        //ONLY IF EXPERT MODE IS SELECTED
        if(EXPERT_MODE) {
            //Initial coin assignment

            //Expert cards drawing
            ExpertsFactory expertsDrawer = ExpertsFactory.createFactory(this);
            this.expertsCard.addAll(expertsDrawer.drawAssistant());
        }

    }

    /**Method to add the player to the current game and automatically set the team mate
     * @param nickname nickname of the player
     * @param assistant assistant deck chosen
     */
    public void addPlayer(String nickname,DeckType assistant, TowerColor towerColor) throws FileNotFoundException {
        this.playersList.add(new Player(nickname,assistant,towerColor,this));
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

       for(int i = 0; i < NUM_OF_PLAYERS; i++)
       {
           try {
               cloudTiles.get(i).setStudents(this.pouch.randomDraw(3));
           } catch (NotEnoughStudentsException e) {
               throw new RuntimeException(e);
           }
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

        //if no one has ever played the color to check
        if (influenceMap.get(colorToCheck).getPlayer() == null) {
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);
        } else if (numOfCheckedStudent > influenceMap.get(colorToCheck).getNumOfStudents()) {

            //Add and remove the teacher from the involved players
            influenceMap.get(colorToCheck).getPlayer().getBoard().removeTeacher(colorToCheck);
            activePlayer.getBoard().addTeachers(colorToCheck);
            //Update influence map data
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);

        }
    }

    /**
     * Method used to check influence on an island tile and setting the player with most influence on the island (if existing)
     *
     * @param islandToCheck island to check the influence over
     */
    public void checkIslandInfluence(Node islandToCheck) {
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

    public int getCoins() {
        return coins;
    }

    public ArrayList<ExpertCard> getExpertsCard() {
        return expertsCard;
    }

    public ArrayList<TowerColor> getTOWER_COLORS_AVAILABLE(){
        return TOWER_COLORS_AVAILABLE;
    }

}
