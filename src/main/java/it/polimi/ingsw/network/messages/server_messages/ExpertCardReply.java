package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**Reply message answering ExpertCardRequest with the info about use card
 * @author Massimo */
public class ExpertCardReply extends Message {
    UUID identifier = UUID.randomUUID();
    private final ArrayList<ExpertCard> expertList;

    public ExpertCardReply(ArrayList<ExpertCard> expertIDList){
        super(MessageType.EXPERT_CARD_REPLY,"Server");
        this.expertList = expertIDList;
    }

    public ArrayList<ExpertCard>  getExpertID() {
        return expertList;
    }
}
