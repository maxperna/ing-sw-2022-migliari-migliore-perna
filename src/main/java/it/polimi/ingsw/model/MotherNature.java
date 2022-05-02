package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class MotherNature, handling the movements onto the island, only one instance of mother nature is possible per each
 * game created;
 * @author Miglia, Massimo
 */
public class MotherNature {

    private IslandTile currentIsland;      //current island whom mother nature is present on
    private static Map<UUID,MotherNature> currentInstantiated = new HashMap<UUID,MotherNature>();       //game which have already instantiated a mother nature


    /**
     * Constructor
     * private so that we cannot instantiate the class
     */
    private MotherNature() {};

    /**
     * Getter, ensures that there will be only one MotherNature, the method return a new mother nature if the game has
     * not already instantiated another tile or return the one instantiated by the caller gameID
     * @param gameID UUID of the game which called the creator
     * @return MotherNature
     */
    public static MotherNature getInstance(UUID gameID) {
        if (currentInstantiated.get(gameID) == null) {
            MotherNature instance = new MotherNature();
            currentInstantiated.put(gameID,instance);

            return instance;
        }
        else
        {
            return currentInstantiated.get(gameID);
        }


    }
    /**
     * @return IslandTile MotherNature is on
     */
    public IslandTile getTile() {

        return currentIsland;
    }
}