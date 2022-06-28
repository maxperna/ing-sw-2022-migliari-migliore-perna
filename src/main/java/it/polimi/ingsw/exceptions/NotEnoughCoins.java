package it.polimi.ingsw.exceptions;

public class NotEnoughCoins extends Exception {
    public NotEnoughCoins() {
        super();
    }

    public NotEnoughCoins(String message) {
        super(message);
    }
}

