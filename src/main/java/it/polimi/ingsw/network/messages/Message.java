package it.polimi.ingsw.network.messages;

import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract class defining the blueprint for a network message
 *
 * @author Massimo
 */
public abstract class Message implements Serializable {
    private final MessageType type;
    private final String senderPlayer;
    UUID identifier = UUID.randomUUID();

    public Message(MessageType type, String senderPlayer) {
        this.type = type;
        this.senderPlayer = senderPlayer;
    }

    public String getSenderPlayer() {
        return senderPlayer;
    }

    public MessageType getType() {
        return type;
    }

    public UUID getIdentifier() {
        return identifier;
    }
}
