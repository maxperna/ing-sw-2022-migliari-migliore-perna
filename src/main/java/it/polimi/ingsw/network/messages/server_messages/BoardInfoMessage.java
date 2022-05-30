package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;

public class BoardInfoMessage extends Message {

    private final Map<String, Board> boardMap;

    public BoardInfoMessage(Map<String, Board> boardMap) {
        super(MessageType.SHOW_BOARD, "Server");
        this.boardMap = boardMap;
    }

    public Map<String, Board> getBoardMap() {
        return boardMap;
    }
}
