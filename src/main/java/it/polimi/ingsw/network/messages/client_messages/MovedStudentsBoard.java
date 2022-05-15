package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class MovedStudentsBoard extends Message {

    private final ArrayList<Color> movedStudents;

    public MovedStudentsBoard(String senderPlayer, ArrayList<Color> movedStudents) {
        super(MessageType.MOVE_TO_DINING, senderPlayer);
        this.movedStudents = movedStudents;
    }

    public ArrayList<Color> getMovedStudents() {
        return movedStudents;
    }
}
