package it.polimi.ingsw.exceptions;
/**Exception thrown when an illegal move is attempted
 * */
public class IllegalMove extends Exception {
    public IllegalMove(){
        super();
    }
    public IllegalMove(String message){super(message);}
}
