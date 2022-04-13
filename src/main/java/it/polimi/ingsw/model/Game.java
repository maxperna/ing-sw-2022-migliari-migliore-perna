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
    private final UUID gameID;
    private final ArrayList <Player> playersList;
    private final GameField gameField;

    /**
     * Constructor
     */
    public Game(int numberOfPlayers, int maxTowers, int maxStudentHall) {

        this.gameID = UUID.randomUUID();
        this.gameField = new GameField(gameID, numberOfPlayers);

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList <Player> playersCreated = new ArrayList <>();

        switch (numberOfPlayers) {

            case 2:
            {

                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.WHITE));
                playersCreated.add(player2);

                break;
            }

            case 3:
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

            case 4:
            {
                //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
                Player player1 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.BLACK));
                playersCreated.add(player1);

                Player player2 = new Player(new Board(gameID,maxStudentHall,maxTowers,TowerColor.WHITE));
                playersCreated.add(player2);

                Player player3 = new Player(new Board(gameID,maxStudentHall,maxTowers,player2.getBoard().getTowers().get(0),player2.getBoard().getTowers()));
                playersCreated.add(player3);

                Player player4 = new Player(new Board(gameID,maxStudentHall,maxTowers,player1.getBoard().getTowers().get(0),player1.getBoard().getTowers()));
                playersCreated.add(player4);

                break;
            }

            default:
                throw new IllegalArgumentException("Unknown number of players");
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
