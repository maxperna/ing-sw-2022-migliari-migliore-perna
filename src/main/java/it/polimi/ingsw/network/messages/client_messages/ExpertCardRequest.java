package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Message to require info about expert card UseCard() method
 * @author Massimo */
public class ExpertCardRequest extends Message {
    public ExpertCardRequest(String sender){
        super(MessageType.EXPERT_CARD_REQ,sender);
    }

}
