package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an assignable limited space is full (i.e. outside hall, tower space ecc)
 */
public class NotEnoughSpace extends Exception {
    public NotEnoughSpace() {
        super();
    }

    public NotEnoughSpace(String message) {
        super(message);
    }
}
