package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

/**
 * Class TwoPlayers, used by the Strategy for creating two players games
 *
 * @author Miglia
 */
public class TwoPlayers implements Strategy {

    static final int maxStudentHall = 7;
    static final int maxTowers = 8;
    static final int numberOfPlayers = 2;

    /**
     * Method used to generate a new game
     *
     * @return a Game class initialized for two players
     */
    @Override
    public Game generateGame(boolean expertMode) {

        return new Game(numberOfPlayers,expertMode);

    }
}
