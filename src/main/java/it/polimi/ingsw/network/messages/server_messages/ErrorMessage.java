package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class ErrorMessage extends Message {

    private final String errorMessage;
    private final ErrorType messageTypeError;

    public ErrorMessage(String errorMessage, ErrorType errorType) {
        super(MessageType.ERROR, "Server");
        this.errorMessage = errorMessage;
        this.messageTypeError = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getTypeError() {
        return messageTypeError;
    }
}
