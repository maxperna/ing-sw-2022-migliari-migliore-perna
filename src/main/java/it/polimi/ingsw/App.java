package it.polimi.ingsw;

import it.polimi.ingsw.model.strategy.GameManager;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        GameManager.getInstance().startGame("TwoPlayers");
        GameManager.getInstance().startGame("ThreePlayers");
        GameManager.getInstance().startGame("FourPlayers");

        int i = 0;
    }
}
