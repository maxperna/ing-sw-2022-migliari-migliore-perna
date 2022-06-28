package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import java.util.UUID;

/**
 * Method 2 to play expert card with nodeID
 *
 * @author Massimo
 */
public class PlayExpertCard2 extends PlayExpertCard {

    private final int nodeID;
    UUID identifier = UUID.randomUUID();

    public PlayExpertCard2(String senderPlayer, int nodeID, int playedCard) {
        super(2, playedCard, senderPlayer);
        this.nodeID = nodeID;
    }

    public int getNodeID() {
        return nodeID;
    }


}
