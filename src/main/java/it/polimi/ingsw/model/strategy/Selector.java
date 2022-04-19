package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

/**
 * Class Selector, it allows to select which strategy to implement for creating new games
 *
 * @author Miglia
 */
public class Selector {

    private final Strategy strategy;

    /**
     * Public constructor
     */
    public Selector(Strategy strategy) {

        this.strategy = strategy;
    }

    /**
     * Method used to generate a new game with a set strategy
     *
     * @return a Game class initialized with the selected strategy
     */
    public Game CreateGame() {

        return strategy.generateGame();

    }


}
