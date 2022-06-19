package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;

public class GameFieldMessage extends Message {

    private final Map<Integer, Node> gameFieldMap;

    public GameFieldMessage(Map<Integer, Node> gameFieldMap) {
        super(MessageType.GAME_FIELD, "Server");
        this.gameFieldMap = gameFieldMap;
    }

    public Map<Integer, Node> getGameFieldMap() {
        return gameFieldMap;
    }
}
