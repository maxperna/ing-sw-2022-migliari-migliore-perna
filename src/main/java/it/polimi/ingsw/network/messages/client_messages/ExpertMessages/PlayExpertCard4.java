package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;

import java.util.UUID;

/**Method 4 to play expert card, with student
 * @author Massimo */
public class PlayExpertCard4 extends PlayExpertCard {

    UUID identifier = UUID.randomUUID();
    private final Color student;
    public PlayExpertCard4(String senderPlayer,Color student,int playedCard) {
        super(4, playedCard,senderPlayer);
        this.student = student;
    }

    public Color getStudent() {
        return student;
    }

}
