package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

public class MoveMotherNatureMessage extends Message {
    private final int numOfSteps;
    UUID identifier = UUID.randomUUID();

    public MoveMotherNatureMessage(String senderPlayer, int numOfSteps) {
        super(MessageType.MOVE_MOTHER_NATURE, senderPlayer);
        this.numOfSteps = numOfSteps;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }
}
