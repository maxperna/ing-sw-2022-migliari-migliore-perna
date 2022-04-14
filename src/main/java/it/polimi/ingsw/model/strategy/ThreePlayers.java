package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

public class ThreePlayers implements Strategy {

    static final int maxStudentHall = 9;
    static final int maxTowers = 6;
    static final int numberOfPlayers = 3;

    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers, maxTowers, maxStudentHall);
    }
}
