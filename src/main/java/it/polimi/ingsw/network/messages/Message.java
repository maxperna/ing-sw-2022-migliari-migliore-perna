package it.polimi.ingsw.network.messages;

import java.io.Serializable;
import java.util.UUID;

/**Abstract class defining the blueprint for a network message
 * @author Massimo*/
public class Message implements Serializable {
    UUID identifier = UUID.randomUUID();
    private final MessageType type;
    private final String senderPlayer;

    public Message(MessageType type, String senderPlayer){
        this.type = type;
        this.senderPlayer = senderPlayer;
    }

    public String getSenderPlayer() {
        return senderPlayer;
    }

    public MessageType getType() {
        return type;
    }

    public UUID getIdentifier(){
        return  identifier;
    }
}
