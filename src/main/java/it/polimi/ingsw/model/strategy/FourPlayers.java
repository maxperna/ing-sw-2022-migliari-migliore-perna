package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

public class FourPlayers implements Strategy {

    static final int maxStudentHall = 7;
    static final int maxTowers = 8;
    static final int numberOfPlayers = 4;

    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers,maxTowers,maxStudentHall);

    }
}
