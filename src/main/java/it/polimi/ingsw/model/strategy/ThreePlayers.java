package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

/**
 * Class ThreePlayers, used by the Strategy for creating three players games
 *
 * @author Miglia
 */
public class ThreePlayers implements Strategy {

    static final int maxStudentHall = 9;
    static final int maxTowers = 6;
    static final int numberOfPlayers = 3;

    /**
     * Method used to generate a new game
     *
     * @return a Game class initialized for three players
     */
    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers, maxTowers, maxStudentHall);
    }
}
