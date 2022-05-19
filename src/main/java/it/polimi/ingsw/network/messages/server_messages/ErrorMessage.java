package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class ErrorMessage extends Message {

    private final String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(MessageType.ERROR, "Server");
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
