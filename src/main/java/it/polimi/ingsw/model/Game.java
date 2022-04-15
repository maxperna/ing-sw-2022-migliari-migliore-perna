package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughElements;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 *
 * @author Miglia
 */
public class Game {

    /**Class used for define a new type of data for influence map(tuple)
     * */
    public static class Pair<Player,Integer>{
        private  Player player;
        private Integer numOfStudents;

        Pair(Player x, Integer y){
            this.player = x;
            this.numOfStudents = y;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public void setNumOfStudents(Integer numOfStudents) {
            this.numOfStudents = numOfStudents;
        }

        public Player getPlayer() {
            return player;
        }

        public Integer getNumOfStudents() {
            return numOfStudents;
        }
    }
    public final static int maxTile = 12;
    private final UUID gameID;
    private final ArrayList<Player> playersList;
    private final GameField gameField;
    public final int NUM_OF_PLAYERS;
    private HashMap<Color,Pair<Object,Integer>> influenceMap;

    /**
     * Constructor
     */
    public Game(int numberOfPlayers, int maxTowers, int maxStudentHall) {

        this.gameID = UUID.randomUUID();
        this.gameField = new GameField(gameID, numberOfPlayers);
        this.NUM_OF_PLAYERS = numberOfPlayers;

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList<Player> playersCreated = new ArrayList<>();

        switch (numberOfPlayers) {

            case 2: {

                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                break;
            }

            case 3: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.GRAY));
                playersCreated.add(player3);

                break;
            }

            case 4: {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID, maxStudentHall, maxTowers, TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(gameID, maxStudentHall, maxTowers, player2.getBoard().getTowers().get(0), player2.getBoard().getTowers()));
                playersCreated.add(player3);

                Player player4 = new Player(new Board(gameID, maxStudentHall, maxTowers, player1.getBoard().getTowers().get(0), player1.getBoard().getTowers()));
                playersCreated.add(player4);

                break;
            }

            default:
                throw new IllegalArgumentException("Unknown number of players");
        }

        //Creates a new list of player
        this.playersList = playersCreated;

        //Initialization of influence map, at the beginning player with most influence is null
        for(Color color : Color.values()){
                Pair<Object, Integer> internalPair = new Pair<>(null,0);
                influenceMap.put(color,internalPair);
            }

    }


    public static ArrayList<Color> drawFromPool(int arrayListLength, @NotNull ArrayList<Color> arrayList) throws NotEnoughElements {


        if (arrayList.isEmpty())
            return null;

        if (arrayListLength > arrayList.size())
            throw new NotEnoughElements();

        ArrayList<Color> randomDraw = new ArrayList<>();

        Collections.shuffle(arrayList);
        for (int i = 0; i < arrayListLength; i++) {
            randomDraw.add(arrayList.remove(i));
        }

        return randomDraw;
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
    public GameField getGameField() {
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

    /**Method used to check influence over inside hall of a player, it automatically set the teacher on the board if
     * needed
     * @param activePlayer player who has changed the number of student
     * @param colorToCheck color to check the influence over
     * */
    public void checkInfluence(Player activePlayer, Color colorToCheck){
        int numOfCheckedStudent = activePlayer.getBoard().colorStudent(colorToCheck);
        if(numOfCheckedStudent>influenceMap.get(colorToCheck).getNumOfStudents()){
            //Add and remove the teacher from the involved player
            influenceMap.get(colorToCheck).getPlayer().getBoard();
            activePlayer.getBoard().addTeachers(colorToCheck);
            //Update influence map data
            influenceMap.get(colorToCheck).setPlayer(activePlayer);
            influenceMap.get(colorToCheck).setNumOfStudents(numOfCheckedStudent);

        }
    }
}
