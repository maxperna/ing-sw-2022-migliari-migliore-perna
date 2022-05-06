package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.gameField.IslandList;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 *
 * @author Miglia
 */
public class Game {

    public final static int MAX_TILE = 12;
    public final boolean EXPERT_MODE;       //set to true if expert mode is selected
    public final int NUM_OF_PLAYERS;
    private final UUID gameID;
    private final ArrayList<Player> playersList;
    private final HashMap<Board,Player> boardAssignations = new HashMap<>();    //Map to keep track of boards assignations
    private final IslandList gameField;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private static final HashMap<Color, Pair<Player, Integer>> influenceMap = new HashMap<>(); //mapping the influence of every player
    private final ArrayList<ExpertCard> expertsCard = new ArrayList<>();
    public int coins = 20;

    /**
     * Constructor
     *
     * @param numberOfPlayers number of players in the match
     * @param maxTowers       number of towers for each player
     * @param maxStudentHall  number of students for each player at the beginning of the game
     */
    public Game(int numberOfPlayers, int maxTowers, int maxStudentHall,boolean expertMode) {

        this.gameID = UUID.randomUUID();
        this.NUM_OF_PLAYERS = numberOfPlayers;
        this.pouch = new Pouch();
        this.gameField = new IslandList();
        this.EXPERT_MODE = expertMode;

        //creates cloudTiles
        ArrayList <CloudTile> cloudTileList = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            cloudTileList.add(new CloudTile(i));
        }
        this.cloudTiles = cloudTileList;


        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList<Player> playersCreated = new ArrayList<>();
        Board createdBoard;      //container variable to keep the created board for each player

        switch (numberOfPlayers) {
            case 2: {

                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                createdBoard = new Board(this, maxStudentHall, maxTowers, TowerColor.BLACK);
                Player player1 = new Player(createdBoard);
                playersCreated.add(player1);
                boardAssignations.put(createdBoard,player1);

                createdBoard = new Board(this, maxStudentHall, maxTowers, TowerColor.WHITE);
                Player player2 = new Player(createdBoard);
                playersCreated.add(player2);
                boardAssignations.put(createdBoard,player2);

                break;
            }

            case 3: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                createdBoard = new Board(this, maxStudentHall, maxTowers, TowerColor.BLACK);
                Player player1 = new Player(createdBoard);
                playersCreated.add(player1);
                boardAssignations.put(createdBoard,player1);

                createdBoard = new Board(this, maxStudentHall, maxTowers, TowerColor.WHITE);
                Player player2 = new Player(createdBoard);
                playersCreated.add(player2);
                boardAssignations.put(createdBoard,player2);

                new Board(this, maxStudentHall, maxTowers, TowerColor.GRAY);
                Player player3 = new Player(createdBoard);
                playersCreated.add(player3);
                boardAssignations.put(createdBoard,player3);

                break;
            }

            case 4: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                createdBoard = new Board(this, maxStudentHall, maxTowers, TowerColor.BLACK);
                Player player1 = new Player(createdBoard);
                playersCreated.add(player1);
                boardAssignations.put(createdBoard,player1);

                createdBoard=new Board(this, maxStudentHall, maxTowers, TowerColor.WHITE);
                Player player2 = new Player(createdBoard);
                playersCreated.add(player2);
                boardAssignations.put(createdBoard,player2);

                createdBoard = new Board(this, maxStudentHall, player2.getBoard().getNumOfTowers(), player2.getTowerColor(), player2);
                Player player3 = new Player(createdBoard);
                playersCreated.add(player3);
                boardAssignations.put(createdBoard,player3);

                createdBoard = new Board(this, maxStudentHall, player1.getBoard().getNumOfTowers(), player1.getTowerColor(), player1);
                Player player4 = new Player(createdBoard);
                playersCreated.add(player4);
                boardAssignations.put(createdBoard,player4);

                break;
            }

            default:
                throw new IllegalArgumentException("Unknown number of players");
        }

        //Creates a new list of player
        this.playersList = playersCreated;

        //Initialization of influence map, at the beginning player with most influence is null
        for (Color color : Color.values()) {
            Pair<Player, Integer> internalPair = new Pair<>(null, 0);
            influenceMap.put(color, internalPair);
        }

        //ONLY IF EXPERT MODE IS SELECTED
        if(EXPERT_MODE) {
            //Initial coin assignment
            for (Player player : this.playersList) {
                this.coinHandler(player, 1);          //assign the initial coin to every player
            }

            //Expert cards drawing
            ExpertsFactory expertsDrawer = ExpertsFactory.createFactory(this);
            this.expertsCard.addAll(expertsDrawer.drawAssistant());
        }

    }


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

    public HashMap<Board, Player> getBoardAssignations() {
        return boardAssignations;
    }

    public Pouch getPouch() {
        return pouch;
    }

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

    public ArrayList<CloudTile> getCloudTiles() {
        return cloudTiles;
    }


    public int getCoins() {
        return coins;
    }

    public ArrayList<ExpertCard> getExpertsCard() {
        return expertsCard;
    }


    public void setCoins(int coins) {
        this.coins = coins;
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
                //If player to check is null no one ha still the influence on that color
                if (playerToCheck != null) {
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
                //If the temporary influence counter is empty no one has influence
                if (!temporaryInfluenceCounter.isEmpty()) {
                    //check the player with most influence
                    Player maxInfluencePlayer = temporaryInfluenceCounter.entrySet().stream().max((val1, val2) ->
                            val1.getValue() > val2.getValue() ? 1 : -1).get().getKey();

                    islandToCheck.setMostInfluencePlayer(maxInfluencePlayer);
                }

            }
        }
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
    };

}
