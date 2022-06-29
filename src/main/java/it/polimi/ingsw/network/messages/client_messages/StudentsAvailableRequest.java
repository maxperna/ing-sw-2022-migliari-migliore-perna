package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Message used to create a request for all the students available inside the entry hall of a board
 */
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