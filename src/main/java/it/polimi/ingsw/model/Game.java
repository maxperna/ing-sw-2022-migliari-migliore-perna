package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.*;
import it.polimi.ingsw.model.factory.GameFactory;
import it.polimi.ingsw.model.factory.GameFieldCreator;

import java.util.ArrayList;
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


    public void startGame(String gameMode) {

        int numberOfPlayers;
        int numberOfDifferentTowers;

        //Crea una nuova lista di giocatori
        this.playersList = new ArrayList<Player>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        this.gameFactory = new GameFactory();
        StudentCreator playersCreator;
        GameFieldCreator gameFieldCreator;

        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");
        else {
            //setta la factory in base alla modalita di gioco selezionata
            switch (gameMode) {
                case "TwoPlayers":
                case "ThreePlayers":


                case "FourPlayers":
                    playersCreator = this.gameFactory.createPlayers(gameMode);
                    gameFieldCreator = this.gameFactory.createField(gameMode);

                    this.playersList = playersCreator.createPlayers();

                    for (Player player : playersList) {
                        System.out.println("-------- Player ----------------------------------------------------------------------------");
                        for (int i = 0; i < player.getBoard().getStudentsOutside().size(); i++)
                            System.out.println(player.getBoard().getStudentsOutside().get(i).getColor());
                        for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                            System.out.println(player.getBoard().getTowers().get(k).getColor());
                    }

                    break;

                default:
                    throw new IllegalArgumentException("Unknown selector " + gameMode);
            }

            System.out.println("numero di giocatori " + playersList.size());
            //Crea il GameField
            gameFieldCreator.newField();
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

    /**
     * Getter
     * @return The list of players in this match
     */
    public ArrayList<Player> getPlayersList() {
        return playersList;
    }
}
