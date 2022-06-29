package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the chosen card is not present in the card deck
 */
public class NonexistentCard extends Exception {
    public NonexistentCard() {
        super();
    }

    public NonexistentCard(String message) {
        super(message);
    }
}
