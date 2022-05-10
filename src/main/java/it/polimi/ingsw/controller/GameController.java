package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.FirstLoginMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static it.polimi.ingsw.network.messages.MessageType.FIRST_LOGIN;
import static it.polimi.ingsw.network.messages.MessageType.LOGIN;

public class GameController {

    private Game game;
    private final Map<UUID,VirtualView> viewMap;
    private Player currentPlayer;
    private GameState gameState;

    public GameController(String gameMode, boolean expertMode) {

        this.game = null;
        this.currentPlayer = null;
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.gameState = GameState.LOGIN;
    }

    public void onMessageReceived (Message receivedMessage) {

        VirtualView virtualView = viewMap.get(currentPlayer.getPlayerID());

        switch (gameState)  {
            case LOGIN:
                loginState(receivedMessage);
        }
    }

    private void loginState (Message receivedMessage){
        if (receivedMessage.getType() == FIRST_LOGIN){
            game = GameManager.getInstance().initGame(((FirstLoginMessage) receivedMessage).getGameMode(), ((FirstLoginMessage) receivedMessage).isExpertMode());
            game.addPlayerNickName(receivedMessage.getSenderPlayer());
        }
        else if (receivedMessage.getType() == LOGIN) {
            game.addPlayerNickName(receivedMessage.getSenderPlayer());
        }
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

    public Game getGame() {
        return game;
    }
}
