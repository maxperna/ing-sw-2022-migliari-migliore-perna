package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

public class Selector {

    private final Strategy strategy;

    public Selector(Strategy strategy) {

        this.strategy = strategy;
    }

    public Game CreateGame() {

        return strategy.generateGame();

    }


}
