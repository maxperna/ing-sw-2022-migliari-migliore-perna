package it.polimi.ingsw.model;

/**
 * Enum for the colors of the towers
 *
 * @author Miglia
 */
public enum TowerColor {
    WHITE("images/Scontornate/white_tower.png"),
    BLACK("images/Scontornate/black_tower.png"),
    GRAY("images/Scontornate/grey_tower.png"),

    EMPTY("");

    private final String towerImg;

    /**
     * Setter
     * @param towerImg is the file name for the tower image
     */
    TowerColor(String towerImg) {
        this.towerImg = towerImg;
    }

    /**
     * Getter
     * @return the tower image
     */
    public String getTowerImg() {
        return towerImg;
    }
}