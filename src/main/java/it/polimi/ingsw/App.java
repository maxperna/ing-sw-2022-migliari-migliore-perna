package it.polimi.ingsw;
import it.polimi.ingsw.model.*;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Game game1 = new Game();
        System.out.println("GameID: "+game1.getGameID());

        game1.startGame("TwoPlayers");
        game1.startGame("ThreePlayers");
        game1.startGame("FourPlayers");


    }
}
