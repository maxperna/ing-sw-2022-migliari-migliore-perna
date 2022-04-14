package it.polimi.ingsw.model;

//Class card, implementing his feature from a json file
public class Card {
    private final int actionNumber;               //number which determinate the first player to move
    private final int motherNatureControl;       //maximum number of step of mother nature
    private final String frontImage;               //path for front image
    private final String backImage;                 //path for back image

    public Card(int actionNumber, int motherNatureControl, String frontImage, String backImage){
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

    public String getFrontImage() {
        return frontImage;
    }

    public String getBackImage() {
        return backImage;
    }
}
