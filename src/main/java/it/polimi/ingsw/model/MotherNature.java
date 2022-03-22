package it.polimi.ingsw.model;

//Singleton, the only way to call MotherNature is to use getInstance
public class MotherNature {

    private IslandTile tile;

    //create an object of MotherNature
    private static MotherNature instance = null;

    //private constructor so that we cannot instantiate the class
    private MotherNature() {};

    //returns the only available object
    public static MotherNature getInstance() {
        if (instance == null)
            instance = new MotherNature();

        return instance;
    }

    //returns the tile mother nature is on
    public IslandTile getTile() {

        return tile;
    }
}