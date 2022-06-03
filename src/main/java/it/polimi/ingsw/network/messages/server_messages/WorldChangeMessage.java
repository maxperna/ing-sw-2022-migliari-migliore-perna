package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.network.messages.MessageType.WORLD_CHANGE;

public class WorldChangeMessage extends Message {

    private final Map<Integer, Node> gameFieldMap;

    private final ArrayList<CloudTile> chargedClouds;

    public WorldChangeMessage(Map<Integer, Node> gameFieldMap, ArrayList<CloudTile> chargedClouds) {
        super(WORLD_CHANGE, "Server");
        this.gameFieldMap = gameFieldMap;
        this.chargedClouds = chargedClouds;
    }

    public Map<Integer, Node> getGameFieldMap() {
        return gameFieldMap;
    }

    public ArrayList<CloudTile> getChargedClouds() {
        return chargedClouds;
    }
}
