package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.List;


public class TeacherListMessage extends Message {

    private final List<Color> teacherList;

    public TeacherListMessage(List<Color> teacherList) {
        super(MessageType.UPDATE_TEACHERS, "Server");

        this.teacherList = teacherList;
    }

    public List<Color> getTeacherList() {
        return teacherList;
    }
}
