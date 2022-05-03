package it.polimi.ingsw.exceptions;

//Exception launched when an assignable limited space is full (i.e. outside hall, tower sapce ecc)
public class NotEnoughSpace extends Exception{
    public NotEnoughSpace(){
        super();
    }
    public NotEnoughSpace(String message){
        super(message);
    }
}
