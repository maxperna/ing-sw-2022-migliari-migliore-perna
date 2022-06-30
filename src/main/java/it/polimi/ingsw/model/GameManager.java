package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.model.strategy.FourPlayers;
import it.polimi.ingsw.model.strategy.Selector;
import it.polimi.ingsw.model.strategy.ThreePlayers;
import it.polimi.ingsw.model.strategy.TwoPlayers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class GameManager, singleton
 * stores and creates new instances of Game class
 *
 * @author Miglia
 */
public class GameManager {

    private static GameManager gameManager = null;
    private final ArrayList<Game> gamesList;


    /**
     * Private constructor
     */
    private GameManager() {
        this.gamesList = new ArrayList<>();
        ArrayList<GameController> controllerList = new ArrayList<>();
    }

    /**
     * Method used to get the only instance of gameManager
     *
     * @return GameManager instance
     */
    public synchronized static GameManager getInstance() {

        if (gameManager == null)
            gameManager = new GameManager();

        return gameManager;
    }

    /**
     * Method used to draw a random poll of students from an Array passed as parameter, used to initialize the game.
     *
     * @param arrayListLength length of the array that will be created, must be lower than the length of the array passed as parameter
     * @param arrayList       ArrayList of Color
     * @return an ArrayList of "arrayListLength" dimensions in random order
     */
    public static ArrayList<Color> drawFromPool(int arrayListLength, @NotNull ArrayList<Color> arrayList) throws NotEnoughElements {


        if (arrayList.isEmpty())
            return new ArrayList<>();

        if (arrayListLength > arrayList.size())
            throw new NotEnoughElements();

        ArrayList<Color> randomDraw = new ArrayList<>();

        Collections.shuffle(arrayList);
        for (int i = 0; i < arrayListLength; i++) {
            randomDraw.add(arrayList.remove(0));
        }

        return randomDraw;
    }

    public static void setNull() {
        gameManager = null;
    }

    /**
     * Method used to initialize the games
     *
     * @param gameMode   used to select the number of player in the game
     * @param expertMode selector for expert mode
     */
    public synchronized Game initGame(@NotNull String gameMode, boolean expertMode) {

        Selector selector;

        switch (gameMode) {

            case "TwoPlayers": {
                selector = new Selector(new TwoPlayers());
                break;
            }

            case "FourPlayers": {
                selector = new Selector(new FourPlayers());
                break;
            }

            case "ThreePlayers": {
                selector = new Selector(new ThreePlayers());
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown selector " + gameMode);
        }

        Game game = selector.CreateGame(expertMode);
        gamesList.add(game);
        return game;
    }

    /**
     * Getter
     *
     * @return The list of games currently active
     */
    public ArrayList<Game> getGamesList() {
        return gamesList;
    }

    /**
     * Getter
     *
     * @param i index in the gameList
     * @return game selected
     */
    public Game getGame(int i) {

        if (GameManager.getInstance().getGamesList().size() <= i)
            throw new ArrayIndexOutOfBoundsException();

        return GameManager.getInstance().getGamesList().get(i);
    }


}
