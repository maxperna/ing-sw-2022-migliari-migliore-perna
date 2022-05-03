package it.polimi.ingsw.model;

/**Class to create a single passing parameters to its constructor, card ID is given by action number(unique for each deck)
 * @author Massimo
 */
public class Card {
    private final int actionNumber;               //number which determinate the first player to move
    private final int motherNatureControl;       //maximum number of step of mother nature
    private final String frontImage;               //path for front image
    private final String backImage;                 //path for back image

    /**Card constructor
     * @param actionNumber number in the top left corner of the card
     * @param motherNatureControl number in the top right corner card
     * @param frontImage path of the front image of the card
     * @param backImage  path of the back image of the card
     * */
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
