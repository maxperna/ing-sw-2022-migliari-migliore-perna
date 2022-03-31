package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.*;
import it.polimi.ingsw.model.factory.GameFactory;
import it.polimi.ingsw.model.factory.GameFieldCreator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 * @author Miglia
 */
public class Game {
    public final static int maxPlayers = 4;
    private final UUID gameID;
    private ArrayList<Player> playersList;
    private  GameField gameField;
    private GameFactory gameFactory;

    /**
     * Constructor
     */
    public Game() {
        this.gameID = UUID.randomUUID();
    }

    /**
     * startGame
     * Method that set the game, creates a list of players and a game field
     * @param gameMode used to select the set of rules to use
     */
    public void startGame(String gameMode) {

        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");

        gameMode = gameMode.toUpperCase(Locale.ROOT);
        //Crea una nuova lista di giocatori
        this.playersList = new ArrayList <>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        this.gameFactory = new GameFactory();
        PlayerCreator playersCreator;
        GameFieldCreator gameFieldCreator;

        //setta la factory in base alla modalita di gioco selezionata
        switch (gameMode) {
            case "TWOPLAYERS":
            case "THREEPLAYERS":
            case "FOURPLAYERS":

                playersCreator = this.gameFactory.createPlayers(gameMode);
                gameFieldCreator = this.gameFactory.createField(gameMode);
                break;

            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

        this.playersList = playersCreator.createPlayers();

        //Printa i risultati - da eliminare
        System.out.println("numero di giocatori " + playersList.size());
        for (Player player : playersList) {
            System.out.println("-------- Player ----------------------------------------------------------------------------");
            for (int i = 0; i < player.getBoard().getStudentsOutside().size(); i++)
                System.out.println(player.getBoard().getStudentsOutside().get(i).getColor());
            for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                System.out.println(player.getBoard().getTowers().get(k).getColor());
        }

        //Crea il GameField
        gameFieldCreator.newField();

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

    /**
     * Getter
     * @return The list of players in this match
     */
    public ArrayList<Player> getPlayersList() {
        return playersList;
    }
}
