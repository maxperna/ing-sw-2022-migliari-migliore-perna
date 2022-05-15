package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class CurrentPlayerMessage extends Message {

    private final String currentPlayer;

    public CurrentPlayerMessage(String currentPlayer) {
        super(MessageType.CURRENT_PLAYER, "Sono Beppe Sala");
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
