package it.polimi.ingsw.model;

/**
 * Enum for the colors of the towers
 * @author Miglia
 */
public enum TowerColor {
    WHITE("images/Scontornate/white_tower.png"),
    BLACK("images/Scontornate/black_tower.png"),
    GRAY("images/Scontornate/grey_tower.png"),

    EMPTY("");

    private final String towerImg;

    TowerColor(String towerImg){
        this.towerImg = towerImg;
    }

    public String getTowerImg() {
        return towerImg;
    }
}