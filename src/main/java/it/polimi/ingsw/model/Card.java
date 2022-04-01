package it.polimi.ingsw.model;
import java.awt.*;


//Class card, implementing his feature from a json file
public class Card {
    private final int actionNumber;               //number which determinate the first player to move
    private final int motherNatureControl;       //maximum number of step of mother nature
    private final Image frontImage;
    private final Image backImage;

    public Card(int actionNumber, int motherNatureControl, Image frontImage, Image backImage){
        this.actionNumber = actionNumber;
        this.motherNatureControl = motherNatureControl;
        this.frontImage = frontImage;
        this.backImage = backImage;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public int getMotherNatureControl() {
        return motherNatureControl;
    }

    public Image getFrontImage() {
        return frontImage;
    }

    public Image getBackImage() {
        return backImage;
    }
}
