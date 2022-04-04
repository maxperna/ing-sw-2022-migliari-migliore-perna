package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.GameFactory;
import it.polimi.ingsw.model.factory.GameFieldCreator;
import it.polimi.ingsw.model.factory.PlayerCreator;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 *
 * @author Miglia
 */
public class Game {

    public final static int maxTile = 12;
    private final UUID gameID;
    private final GameFactory gameFactory;
    private ArrayList <Player> playersList;
    private GameField gameField;

    /**
     * Constructor
     */
    public Game() {
        this.gameID = UUID.randomUUID();
        this.gameFactory = new GameFactory();
    }

    /**
     * startGame
     * Method that set the game, creates a list of players and a game field
     *
     * @param gameMode used to select the set of rules to use
     */
    public void createGame(String gameMode) {

        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");

        //Crea una nuova lista di giocatori
        this.playersList = new ArrayList <>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        PlayerCreator playersCreator;
        GameFieldCreator gameFieldCreator;

        //setta la factory in base alla modalita di gioco selezionata
        switch (gameMode) {
            case "TwoPlayers":
            case "ThreePlayers":
            case "FourPlayers":

                playersCreator = this.gameFactory.createPlayers(gameMode);
                gameFieldCreator = this.gameFactory.createField(gameMode);
                break;

            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

        //Crea i giocatori
        this.playersList = playersCreator.createPlayers(this.gameID);

        //Crea il GameField
        this.gameField = gameFieldCreator.newField(this.gameID);

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
     * @return GameFactory
     */
    public GameFactory getGameFactory() {
        return gameFactory;
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
