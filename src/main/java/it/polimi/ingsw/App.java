package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args )
    {
        GameController gameController = new GameController();
        gameController.initGame(args[0], Boolean.parseBoolean(args[1]));
    }
}
