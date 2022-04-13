package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {

    private static GameManager gameManager = null;
    private final ArrayList<Game> gamesList;

    private GameManager() {

        this.gamesList = new ArrayList <>();
    }

    public synchronized static GameManager getInstance() {

        if (gameManager == null)
            gameManager = new GameManager();

        return gameManager;
    }

    public synchronized void startGame(@NotNull String gameMode) {

        Selector selector;

        switch (gameMode) {

            case "TwoPlayers":
            {
                selector = new Selector(new TwoPlayers());
                break;
            }

            case "FourPlayers": {
                selector = new Selector(new FourPlayers());
                break;
            }

            case "ThreePlayers":
            {
                selector = new Selector(new ThreePlayers());
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

        Game game = selector.CreateGame();
        gamesList.add(game);
    }

    public ArrayList <Game> getGamesList() {
        return gamesList;
    }
}
