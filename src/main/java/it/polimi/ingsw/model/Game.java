package it.polimi.ingsw.model;

import it.polimi.ingsw.circularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

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
    public final static int INIT_COINS = 20;
    public final int NUM_OF_PLAYERS;
    private final UUID gameID;
    private final ArrayList<Player> playersList;
    private final IslandList gameField;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private static final HashMap<Color, Pair<Player, Integer>> influenceMap = new HashMap<>(); //mapping the influence of every player

    private int coins;

    /**
     * Constructor
     *
     * @param numberOfPlayers number of players in the match
     * @param maxTowers       number of towers for each player
     * @param maxStudentHall  number of students for each player at the beginning of the game
     */
    public Game(int numberOfPlayers, int maxTowers, int maxStudentHall) {

        this.gameID = UUID.randomUUID();
        this.NUM_OF_PLAYERS = numberOfPlayers;
        this.pouch = new Pouch();
        this.gameField = new IslandList();
        this.coins = INIT_COINS;

        //creates cloudTiles
        ArrayList <CloudTile> cloudTileList = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            cloudTileList.add(new CloudTile(i));
        }
        this.cloudTiles = cloudTileList;


        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList<Player> playersCreated = new ArrayList<>();
        switch (numberOfPlayers) {
            case 2: {

                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(pouch, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                break;
            }

            case 3: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.GRAY));
                playersCreated.add(player3);

                break;
            }

            case 4: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, player2.getBoard().getTowers().get(0), player2.getBoard().getTowers()));
                playersCreated.add(player3);

                Player player4 = new Player(new Board(this.pouch, maxStudentHall, maxTowers, player1.getBoard().getTowers().get(0), player1.getBoard().getTowers()));
                playersCreated.add(player4);

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
    public void checkIslandInfluence(IslandTile islandToCheck) {
        HashMap<Player, Integer> temporaryInfluenceCounter = new HashMap<>();  //temporary influence counter

        for (Color colorStudent : islandToCheck.getStudents()) {
            Player playerToCheck = influenceMap.get(colorStudent).getPlayer();
            //If player to check is null no one ha still the influence on that color
            if (playerToCheck != null) {
                int influenceOfPlayer = islandToCheck.colorStudent(colorStudent);       //temp variable storing the number of student of the same color
                //Check if there is a tower, if true add another point of influence
                if (playerToCheck.getBoard().getTowerColor().equals(islandToCheck.getTowerColor()))
                    influenceOfPlayer++;
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

}
