package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Method 4 to play expert card, with student
 * @author Massimo */
public class PlayExpertCard4 extends Message {

    UUID identifier = UUID.randomUUID();
    private final Color student;
    public PlayExpertCard4(String senderPlayer,Color student) {
        super(MessageType.PLAY_EXPERT_CARD, senderPlayer);
        this.student = student;
    }

    public Color getStudent() {
        return student;
    }
}
