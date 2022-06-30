package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the card chosen has been already played by another player in this turn
 */
public class CardAlreadyPlayed extends Exception {
    public CardAlreadyPlayed() {
        super();
    }

    public CardAlreadyPlayed(String message) {
        super(message);
    }

}
