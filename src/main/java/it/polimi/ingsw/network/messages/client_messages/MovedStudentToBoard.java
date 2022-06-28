package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Message notifying the movement of a student into the dinner room
 *
 * @author Massimo
 */
public class MovedStudentToBoard extends Message {

    private final Color movedStudent;

    public MovedStudentToBoard(String senderPlayer, Color movedStudent) {
        super(MessageType.MOVE_TO_DINING, senderPlayer);
        this.movedStudent = movedStudent;
    }

    public Color getMovedStudent() {
        return movedStudent;
    }
}
