package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class AvailableStudentsReply extends Message {

    private final ArrayList<Color> availableStudents;
    private final MessageType typeOfMovement;

    private final int islandSize;

    public AvailableStudentsReply(ArrayList<Color> availableStudents, MessageType typeOfMovement, int islandSize) {
        super(MessageType.STUDENT_REPLY, "Server");
        this.typeOfMovement = typeOfMovement;
        this.availableStudents = availableStudents;
        this.islandSize = islandSize;
    }

    public ArrayList<Color> getAvailableStudents() {
        return availableStudents;
    }

    public MessageType getTypeOfMovement() {
        return typeOfMovement;
    }

    public int getIslandSize() {
        return islandSize;
    }
}
