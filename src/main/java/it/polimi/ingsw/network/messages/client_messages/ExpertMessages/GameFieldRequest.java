package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.server_messages.GameFieldMessage;

public class GameFieldRequest extends Message {
    public GameFieldRequest(String senderPlayer){
        super(MessageType.GAME_FIELD,senderPlayer);
    }
}
