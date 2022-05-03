package it.polimi.ingsw;

import it.polimi.ingsw.model.GameManager;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        GameManager.getInstance().startGame("TwoPlayers",false);
        GameManager.getInstance().startGame("ThreePlayers",false);
        GameManager.getInstance().startGame("FourPlayers",false);

        int i = 0;
    }
}
