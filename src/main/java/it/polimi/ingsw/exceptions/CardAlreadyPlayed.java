package it.polimi.ingsw.exceptions;

//Exception thrown if a card has been played in the same round by a player
public class CardAlreadyPlayed extends Exception{
    public CardAlreadyPlayed(){
        super();
    }
    public CardAlreadyPlayed(String message){
        super(message);
    }

}
