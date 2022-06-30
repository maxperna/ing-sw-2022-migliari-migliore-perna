package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Method 5 to play expert card, with two sets of students
 *
 * @author Massimo
 */
public class PlayExpertCard5 extends PlayExpertCard {

    private final ArrayList<Color> students1;
    private final ArrayList<Color> students2;
    UUID identifier = UUID.randomUUID();

    public PlayExpertCard5(String senderPlayer, ArrayList<Color> students1, ArrayList<Color> students2, int playedCard) {
        super(5, playedCard, senderPlayer);
        this.students1 = students1;
        this.students2 = students2;
    }

    public ArrayList<Color> getStudents1() {
        return students1;
    }

    public ArrayList<Color> getStudents2() {
        return students2;
    }


}
