package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

public class MovedToIslandsMessage extends Message {

    private final HashMap<Integer, ArrayList<Color>> movedStudents;

    public MovedToIslandsMessage(String senderPlayer, HashMap<Integer, ArrayList<Color>> movedStudents) {
        super(MessageType.MOVE_TO_ISLAND, senderPlayer);

        this.movedStudents = movedStudents;
    }

    public HashMap<Integer, ArrayList<Color>> getMovedStudents() {
        return movedStudents;
    }
}
