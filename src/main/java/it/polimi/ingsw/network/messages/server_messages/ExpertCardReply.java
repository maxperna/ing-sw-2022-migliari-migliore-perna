package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**Reply message answering ExpertCardRequest with the info about use card
 * @author Massimo */
public class ExpertCardReply extends Message {
    UUID identifier = UUID.randomUUID();
    private final ArrayList<ExpertID> expertIDList;

    public ExpertCardReply(ArrayList<ExpertID> expertIDList){
        super(MessageType.EXPERT_CARD_REPLY,"Server");
        this.expertIDList = expertIDList;
    }

    public ArrayList<ExpertID>  getExpertID() {
        return expertIDList;
    }
}
