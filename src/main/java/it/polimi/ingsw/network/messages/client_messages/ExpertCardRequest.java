package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Message to require info about expert card UseCard() method
 * @author Massimo */
public class ExpertCardRequest extends Message {
    UUID identifier = UUID.randomUUID();
    private final int cardID;

    public ExpertCardRequest(String sender,int cardID){
        super(MessageType.EXPERT_CARD_REQ,sender);
        this.cardID = cardID;
    }

    public int getCardID() {
        return cardID;
    }
}
