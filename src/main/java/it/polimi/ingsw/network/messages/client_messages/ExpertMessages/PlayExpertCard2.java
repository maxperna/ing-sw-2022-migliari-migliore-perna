package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Method 2 to play expert card with nodeID
 * @author Massimo */
public class PlayExpertCard2 extends Message {

    UUID identifier = UUID.randomUUID();
    private final int nodeID;

    public PlayExpertCard2(String senderPlayer,int nodeID) {
        super(MessageType.PLAY_EXPERT_CARD, senderPlayer);
        this.nodeID = nodeID;
    }

    public int getNodeID() {
        return nodeID;
    }
}
