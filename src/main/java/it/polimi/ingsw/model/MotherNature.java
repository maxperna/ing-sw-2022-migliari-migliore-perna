package it.polimi.ingsw.model;

/**
 * Class MotherNature, Singleton, the only way to call MotherNature is to use getInstance
 * @author Miglia
 */
public class MotherNature {

    private IslandTile tile;

    //create an object of MotherNature
    private static MotherNature instance = null;

    /**
     * Constructor
     * private so that we cannot instantiate the class
     */
    private MotherNature() {};

    /**
     * Getter, ensures that there will be only one MotherNature
     * @return MotherNature
     */
    public static MotherNature getInstance() {
        if (instance == null)
            instance = new MotherNature();

        return instance;
    }

    /**
     * @return IslandTile MotherNature is on
     */
    public IslandTile getTile() {

        return tile;
    }
}