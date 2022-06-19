package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;


public class TeacherListMessage extends Message {

    private final Map<Color, Boolean> teacherList;

    public TeacherListMessage(Map<Color, Boolean> teacherList) {
        super(MessageType.UPDATE_TEACHERS, "Server");

        this.teacherList = teacherList;
    }

    public Map<Color, Boolean> getTeacherList() {
        return teacherList;
    }
}
