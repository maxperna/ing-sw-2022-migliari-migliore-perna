package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Interface PlayerCreator, part of the factory method
 *
 * @author Miglia
 */
public interface PlayerCreator {
    ArrayList <Player> createPlayers(UUID gameID);
}
