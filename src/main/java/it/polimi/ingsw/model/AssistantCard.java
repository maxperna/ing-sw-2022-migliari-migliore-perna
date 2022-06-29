package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Class to create a single passing parameters to its constructor, card ID is given by action number(unique for each deck)
 *
 * @author Massimo
 */
public class AssistantCard implements Serializable {
    private final int actionNumber;               //number which determinate the first player to move
    private final int motherNatureControl;       //maximum number of step of mother nature
    private final String frontImage;               //path for front image
    private final String backImage;                 //path for back image
    private final DeckType deckType;

    /**
     * AssistantCard constructor
     *
     * @param actionNumber        number in the top left corner of the card
     * @param motherNatureControl number in the top right corner card
     * @param frontImage          path of the front image of the card
     * @param backImage           path of the back image of the card
     * @param deckType
     */
    public AssistantCard(int actionNumber, int motherNatureControl, String frontImage, String backImage, DeckType deckType) {
        this.actionNumber = actionNumber;
        this.motherNatureControl = motherNatureControl;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.deckType = deckType;
    }

    /**
     * Method used to get the action number of the card
     * @return the action number used to determine the play order
     */
    public int getActionNumber() {
        return actionNumber;
    }

    /**
     * Method used to get the number of mother nature moves allowed
     * @return the max number of moves that mother nature can perform
     */
    public int getMotherNatureControl() {
        return motherNatureControl;
    }

    /**
     * Method used to get the front image name
     * @return the image name
     */
    public String getFrontImage() {
        return frontImage;
    }

    /**
     * Method used to get the back image name
     * @return the image name
     */
    public String getBackImage() {
        return backImage;
    }

    /**
     * Method used to get the deckType
     * @return the deckType
     */
    public DeckType getDeckType() {
        return deckType;
    }
}
