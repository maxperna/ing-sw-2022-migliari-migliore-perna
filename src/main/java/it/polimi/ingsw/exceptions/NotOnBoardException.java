package it.polimi.ingsw.exceptions;

//Exception thrown when there is an illegal movement trying to move a student which isn't on the board
public class NotOnBoardException extends Exception{
    public NotOnBoardException(){
        super();
    }
    public NotOnBoardException(String message){
        super(message);
    }
}
