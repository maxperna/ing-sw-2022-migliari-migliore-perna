package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class LastCardRequest extends Message {

    public LastCardRequest(String senderPlayer) {
        super(MessageType.LAST_ASSISTANT, senderPlayer);
    }
}
