package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when an action requires more students of a given color than the available
 */
public class NotEnoughStudentsException extends Exception {
    public NotEnoughStudentsException() {
        super();
    }

    public NotEnoughStudentsException(String message) {
        super(message);
    }
}
