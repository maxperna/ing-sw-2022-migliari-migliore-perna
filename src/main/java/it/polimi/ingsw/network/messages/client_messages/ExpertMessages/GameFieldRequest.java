package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class GameFieldRequest extends Message {
    public GameFieldRequest(String senderPlayer) {
        super(MessageType.GAME_FIELD, senderPlayer);
    }
}
