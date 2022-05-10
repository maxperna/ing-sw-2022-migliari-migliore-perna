package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class LogInMessage extends  Message{

    UUID identifier = UUID.randomUUID();

    public LogInMessage(String senderPlayer) {
        super(MessageType.LOGIN, senderPlayer);
    }

}
