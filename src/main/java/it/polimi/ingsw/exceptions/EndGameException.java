package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when one of the conditions to end the game is met
 */
public class EndGameException extends Exception {
    public EndGameException() {
        super();
    }

    public EndGameException(String message) {
        super(message);
    }
}
