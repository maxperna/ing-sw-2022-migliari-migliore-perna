package it.polimi.ingsw.exceptions;

//Exception thrown if the card is not present in the deck
public class NonexistentCard extends Exception {
    public NonexistentCard() {
        super();
    }

    public NonexistentCard(String message) {
        super(message);
    }
}
