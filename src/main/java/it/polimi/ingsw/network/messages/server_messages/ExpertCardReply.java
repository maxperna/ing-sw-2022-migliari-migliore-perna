package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**Reply message answering ExpertCardRequest with the info about use card
 * @author Massimo */
public class ExpertCardReply extends Message {
    private final ArrayList<ExpertCard> expertList;
    private final int numberOfCoins;

    public ExpertCardReply(ArrayList<ExpertCard> expertIDList, int numberOfCoins){
        super(MessageType.EXPERT_CARD_REPLY,"Server");
        this.expertList = expertIDList;
        this.numberOfCoins = numberOfCoins;
    }

    public ArrayList<ExpertCard>  getExpertID() {
        return expertList;
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }
}
