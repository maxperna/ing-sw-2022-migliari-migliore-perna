package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Message used to get the remaining assistant cards from a player's deck
 */
public class AssistantInfoMessage extends Message {
    public AssistantInfoMessage(String senderPlayer) {
        super(MessageType.ASSISTANT_INFO, senderPlayer);
    }
}
