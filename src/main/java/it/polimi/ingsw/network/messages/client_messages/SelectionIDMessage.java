package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

public class SelectionIDMessage extends Message {
    private final int ID;
    UUID identifier = UUID.randomUUID();

    public SelectionIDMessage(String senderPlayer, int ID) {
        super(MessageType.ID_SELECTION, senderPlayer);
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
