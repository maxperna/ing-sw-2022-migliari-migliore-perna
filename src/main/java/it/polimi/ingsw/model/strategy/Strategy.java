package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;

/**
 * Interface Strategy, used to create different types of games based on the number of players
 *
 * @author Miglia
 */
public interface Strategy {

    /**
     * Method used to generate a new game
     *
     * @param expertMode boolean to select the expert modality
     * @return a Game class initialized for a set number of players
     */
    Game generateGame(boolean expertMode);

}
