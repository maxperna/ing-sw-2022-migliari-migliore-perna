package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameController {

    private final Game game;
    private final Map<UUID,VirtualView> viewMap;
    private Player currentPlayer;
    private GameState gameState;

    public GameController(String gameMode, boolean expertMode) {

        this.game = GameManager.getInstance().initGame(gameMode, expertMode);
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.currentPlayer = game.getPlayerFromMap(1);
        this.gameState = GameState.LOGIN;
    }



    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
