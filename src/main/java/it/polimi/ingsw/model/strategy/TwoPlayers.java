package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;
import java.util.Arrays;

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

        ArrayList<TowerColor> towerColors = new ArrayList<>(Arrays.asList(TowerColor.BLACK,TowerColor.WHITE));

        return new Game(numberOfPlayers,expertMode,maxTowers,maxStudentHall, towerColors);

    }
}
