package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.BoardCreator;
import it.polimi.ingsw.model.factory.GameFactory;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 * @author Miglia
 */
public class Game {
    public final static int maxPlayers = 4;
    private final UUID gameID;
    private ArrayList<Player> players;
    private  GameField gameField;
    private GameFactory gameFactory;
    private String board;

    /**
     * Constructor
     */
    public Game() {
        this.gameID = UUID.randomUUID();
    }


    public int startGame(String gameMode) {

        //Crea una nuova lista di giocatori
        this.players = new ArrayList<Player>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        this.gameFactory = new GameFactory();

        //Crea i giocatori, li inserisce nella lista e gli associa una board
        if (gameMode == null || gameMode.isEmpty())
            return 0;
        switch (gameMode) {
            case "TwoPlayers":
                BoardCreator boardCreator2 = this.gameFactory.createBoard(gameMode);
                boardCreator2.newBoard();
                return 2;
            case "ThreePlayers":
                BoardCreator boardCreator3 = this.gameFactory.createBoard(gameMode);
                boardCreator3.newBoard();
                return 3;
            case "FourPlayers":
                BoardCreator boardCreator4 = this.gameFactory.createBoard(gameMode);
                boardCreator4.newBoard();
                return 4;
            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

    }

    /**
     * Getter
     * @return unique ID
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Getter
     * @return the GameField of the match
     */
    public GameField getGameField() {
        return gameField;
    }

    /**
     * Getter
     * @return GameFactory
     */
    public GameFactory getGameFactory() {
        return gameFactory;
    }


    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
