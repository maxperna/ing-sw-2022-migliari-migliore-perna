package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the chosen cloud tile is empty
 */
public class EmptyCloudException extends Exception {
    public EmptyCloudException() {
        super();
    }

    public EmptyCloudException(String message) {
        super(message);
    }
}
