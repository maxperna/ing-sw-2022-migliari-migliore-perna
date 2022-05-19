package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Reply message answering ExpertCardRequest with the info about use card
 * @author Massimo */
public class ExpertCardReply extends Message {
    UUID identifier = UUID.randomUUID();
    private final ExpertID expertID;

    public ExpertCardReply(String senderPlayer,ExpertID expertID){
        super(MessageType.EXPERT_CARD_REPLY,senderPlayer);
        this.expertID = expertID;
    }

    public ExpertID getExpertID() {
        return expertID;
    }
}
