package it.polimi.ingsw.exceptions;

public class NotEnoughCoin extends Exception{
    public NotEnoughCoin(){
        super();
    }
    public NotEnoughCoin(String message){
        super(message);
    }
}

