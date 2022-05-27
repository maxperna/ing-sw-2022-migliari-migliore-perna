package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class CurrentPlayerMessage extends Message {

    private final String currentPlayer;
    private final GameState currentState;

    public CurrentPlayerMessage(String currentPlayer, GameState currentState) {
        super(MessageType.CURRENT_PLAYER, "Server");
        this.currentPlayer = currentPlayer;
        this.currentState = currentState;
    }



    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getCurrentState() {
        return currentState;
    }


}
