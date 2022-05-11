package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

/**
 * Class FourPlayers, used by the Strategy for creating four players games
 *
 * @author Miglia
 */
public class FourPlayers implements Strategy {

    static final int maxStudentHall = 7;
    static final int maxTowers = 8;
    static final int numberOfPlayers = 4;

    /**
     * Method used to generate a new game
     *
     * @return a Game class initialized for four players
     */
    @Override
    public Game generateGame(boolean expertMode) {

        return new Game(numberOfPlayers,expertMode);

    }
}
