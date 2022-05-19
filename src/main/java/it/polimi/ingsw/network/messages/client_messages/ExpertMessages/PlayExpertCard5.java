package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**Method 5 to play expert card, with two sets of students
 * @author Massimo */
public class PlayExpertCard5 extends Message {

    UUID identifier = UUID.randomUUID();
    private final ArrayList<Color> students1;
    private final ArrayList<Color> students2;

    public PlayExpertCard5(String senderPlayer,ArrayList<Color> students1,ArrayList<Color> students2) {
        super(MessageType.PLAY_EXPERT_CARD, senderPlayer);
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
