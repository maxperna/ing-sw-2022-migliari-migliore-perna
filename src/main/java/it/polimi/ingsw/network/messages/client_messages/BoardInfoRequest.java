package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

public class BoardInfoRequest extends Message {

    public BoardInfoRequest(String senderPlayer,String boardPlayer){
        super(MessageType.BOARD_INFO,senderPlayer);
    }

}
