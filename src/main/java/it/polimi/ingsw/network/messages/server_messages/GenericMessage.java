package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class GenericMessage extends Message {

    public GenericMessage(String senderPlayer) {
        super(MessageType.GENERIC, senderPlayer);
    }
}
