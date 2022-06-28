package it.polimi.ingsw.exceptions;

public class EmptyCloudException extends Exception {
    public EmptyCloudException() {
        super();
    }

    public EmptyCloudException(String message) {
        super(message);
    }
}
