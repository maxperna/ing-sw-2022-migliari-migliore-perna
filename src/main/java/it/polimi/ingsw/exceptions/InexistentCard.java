package it.polimi.ingsw.exceptions;

//Exception thrown if the card is not present in the deck
public class InexistentCard extends Exception {
    public InexistentCard() {
        super();
    }

    public InexistentCard(String message) {
        super(message);
    }
}
