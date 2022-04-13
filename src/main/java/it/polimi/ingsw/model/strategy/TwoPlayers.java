package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

public class TwoPlayers implements Strategy {

    static final int maxStudentHall = 7;
    static final int maxTowers = 8;
    static final int numberOfPlayers = 2;

    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers, maxTowers, maxStudentHall);

    }
}
