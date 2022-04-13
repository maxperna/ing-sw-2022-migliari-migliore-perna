package it.polimi.ingsw.model;
import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 *
 * @author Miglia
 */
public class Game {

    public final static int maxTile = 12;
    private final int maxStudentHall = 6;
    private final int maxTowers = 6;
    private final int numberOfPlayers = 2;
    private final UUID gameID;
    private ArrayList <Player> playersList;
    private GameField gameField;

    /**
     * Constructor
     */
    public Game(String gameMode) {

        this.gameID = UUID.randomUUID();
        this.gameField = new GameField(gameID, numberOfPlayers);

        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");


        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList <Player> playersCreated = new ArrayList <>();

        switch (gameMode) {

            case "TwoPlayers":
            {

                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.WHITE));
                playersCreated.add(player2);

                break;
            }

            case "ThreePlayers":
            {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.GRAY));
                playersCreated.add(player3);

                break;
            }

            case "FourPlayers":
            {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(gameID,maxStudentHall,maxTowers,player2.getBoard().getTowers().get(0),player2.getBoard().getTowers()));
                playersCreated.add(player3);

                Player player4 = new Player(new Board(gameID,maxStudentHall,maxTowers,player1.getBoard().getTowers().get(0),player3.getBoard().getTowers()));
                playersCreated.add(player4);

                break;
            }

            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

        //Crea una nuova lista di giocatori
        this.playersList = playersCreated;
    }


    public static ArrayList <Color> drawFromPool(int arrayListLength, @NotNull ArrayList <Color> arrayList) throws NotEnoughElements {


        if (arrayList.isEmpty())
            return null;

        if (arrayListLength > arrayList.size())
            throw new NotEnoughElements();

        ArrayList <Color> randomDraw = new ArrayList <>();

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
    public ArrayList <Player> getPlayersList() {
        return playersList;
    }
}
