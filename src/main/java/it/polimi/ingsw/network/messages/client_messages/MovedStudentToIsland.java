package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;


/**
 * Message notifying the movement of a student to an island
 */
public class MovedStudentToIsland extends Message {

    private final Color movedStudent;
    private final int targetIsland;

    public MovedStudentToIsland(String senderPlayer, Color movedStudent, int targetIsland) {
        super(MessageType.MOVE_TO_ISLAND, senderPlayer);

        this.movedStudent = movedStudent;
        this.targetIsland = targetIsland;
    }

    public Color getMovedStudent() {
        return movedStudent;
    }

    public int getTargetIsland() {
        return targetIsland;
    }
}
