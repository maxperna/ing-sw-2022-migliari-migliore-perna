package it.polimi.ingsw.exceptions;

//Exceptions thrown when the games end
public class EndGameException extends Exception {
    public EndGameException() {
        super();
    }

    public EndGameException(String message) {
        super(message);
    }
}
