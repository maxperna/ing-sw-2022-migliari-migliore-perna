package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the game is not initialized
 */
public class GameNotInitialized extends Exception {
    public GameNotInitialized() {
        super();
    }

    public GameNotInitialized(String message) {
        super(message);
    }
}
