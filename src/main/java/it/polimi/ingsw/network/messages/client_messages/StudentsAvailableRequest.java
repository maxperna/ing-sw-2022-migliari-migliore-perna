package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class StudentsAvailableRequest extends Message {

    private final MessageType typeOfMovement;

    public StudentsAvailableRequest(String senderPlayer, MessageType typeOfMovement) {
        super(MessageType.STUDENT_REQUEST, senderPlayer);
        this.typeOfMovement = typeOfMovement;
    }

    public MessageType getTypeOfMovement() {
        return typeOfMovement;
    }
}