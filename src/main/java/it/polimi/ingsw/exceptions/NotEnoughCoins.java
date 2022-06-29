package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player doesn't have the required number of coins to activate an expert effect
 */
public class NotEnoughCoins extends Exception {
    public NotEnoughCoins() {
        super();
    }

    public NotEnoughCoins(String message) {
        super(message);
    }
}

