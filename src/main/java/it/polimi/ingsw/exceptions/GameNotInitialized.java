package it.polimi.ingsw.exceptions;

public class GameNotInitialized extends Exception{
    public GameNotInitialized(){
        super();
    }
    public GameNotInitialized(String message){super(message);}
}
