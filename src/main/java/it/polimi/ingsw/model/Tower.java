package it.polimi.ingsw.model;

/**
 * Class Tower
 * @author Miglia
 */
public class Tower {

    private final TowerColor color;

    /**
     * Constructor
     * @param color color of the tower that will be created
     */
    public Tower(TowerColor color) {
        this.color = color;
    }

    /**
     * Getter
     * @return color of the tower
     */
    public TowerColor getColor() {
        return color;
    }
}
