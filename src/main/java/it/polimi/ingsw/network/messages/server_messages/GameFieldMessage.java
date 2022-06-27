package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.gameField.IsladNode;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;

public class GameFieldMessage extends Message {

    private final Map<Integer, IsladNode> gameFieldMap;

    public GameFieldMessage(Map<Integer, IsladNode> gameFieldMap) {
        super(MessageType.GAME_FIELD, "Server");
        this.gameFieldMap = gameFieldMap;
    }

    public Map<Integer, IsladNode> getGameFieldMap() {
        return gameFieldMap;
    }
}
