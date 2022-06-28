package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;

import java.util.UUID;

/**
 * Method 3 to play expert card, with nodeId and student
 *
 * @author Massimo
 */
public class PlayExpertCard3 extends PlayExpertCard {

    private final int nodeID;
    private final Color student;
    UUID identifier = UUID.randomUUID();

    public PlayExpertCard3(String senderPlayer, int nodeID, Color student, int playedCard) {
        super(3, playedCard, senderPlayer);
        this.nodeID = nodeID;
        this.student = student;
    }

    public int getNodeID() {
        return nodeID;
    }

    public Color getStudent() {
        return student;
    }

}
