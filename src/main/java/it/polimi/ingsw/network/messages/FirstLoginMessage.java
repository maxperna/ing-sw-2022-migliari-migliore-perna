package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class FirstLoginMessage extends Message {

    UUID identifier = UUID.randomUUID();
    private final boolean expertMode;


    public FirstLoginMessage(String senderPlayer, boolean expertMode) {
        super(MessageType.EXPERTMODE, senderPlayer);
        this.expertMode = expertMode;

    }

    public boolean isExpertMode() {
        return expertMode;
    }

}
