package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;


public class ChargedCloudsRequest extends Message {
    public ChargedCloudsRequest(String senderPlayer) {
        super(MessageType.SHOW_CLOUD, senderPlayer);
    }
}
