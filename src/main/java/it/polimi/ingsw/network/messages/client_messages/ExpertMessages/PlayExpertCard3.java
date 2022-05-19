package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**Method 3 to play expert card, with nodeId and student
 * @author Massimo */
public class PlayExpertCard3 extends PlayExpertCard {

    UUID identifier = UUID.randomUUID();
    private final int nodeID;
    private final Color student;
    public PlayExpertCard3(String senderPlayer,int nodeID,Color student,int playedCard) {
        super(3, playedCard,senderPlayer);
        this.nodeID=nodeID;
        this.student = student;
    }

    public int getNodeID() {
        return nodeID;
    }

    public Color getStudent() {
        return student;
    }

}
