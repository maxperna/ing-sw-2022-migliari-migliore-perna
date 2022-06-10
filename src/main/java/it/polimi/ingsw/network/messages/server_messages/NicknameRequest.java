package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class NicknameRequest extends Message {

    public NicknameRequest(){
        super(MessageType.NICK_REQ,"server");
    }
}
