package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class AssistantInfoMessage extends Message {
    public AssistantInfoMessage(String senderPlayer){
        super(MessageType.ASSISTANT_INFO,senderPlayer);
    }
}
