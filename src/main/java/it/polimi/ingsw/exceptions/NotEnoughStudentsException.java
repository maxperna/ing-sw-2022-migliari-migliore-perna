package it.polimi.ingsw.exceptions;

public class NotEnoughStudentsException extends Exception{
    public NotEnoughStudentsException(){
        super();
    }
    public NotEnoughStudentsException(String message){
        super(message);
    }
}
