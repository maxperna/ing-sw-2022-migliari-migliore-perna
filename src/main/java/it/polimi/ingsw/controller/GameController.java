package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

public class GameController {

    private Game game;

    public void initGame(String gameMode, boolean expertMode) {

        game = GameManager.getInstance().initGame(gameMode, expertMode);

    }
}
