package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Generic message send by server only
 */
public class GenericMessage extends Message {
    private final String body;

    public GenericMessage(String body) {
        super(MessageType.GENERIC, "Server");
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
