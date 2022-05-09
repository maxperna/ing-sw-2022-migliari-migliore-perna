package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.UUID;

/**Abstract class defining the blueprint for a network message
 * @author Massimo*/
public class Message implements Serializable {
    UUID identifier = UUID.randomUUID();
    private final MessageType type;
    private final Player senderPlayer;

    public Message(MessageType type, Player senderPlayer){
        this.type = type;
        this.senderPlayer = senderPlayer;
    }

    public Player getSenderPlayer() {
        return senderPlayer;
    }

    public MessageType getType() {
        return type;
    }

    public UUID getIdentifier(){
        return  identifier;
    }
}
