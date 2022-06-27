package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.gameField.IsladNode;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.network.messages.MessageType.WORLD_CHANGE;

public class WorldChangeMessage extends Message {

    private final Map<Integer, IsladNode> gameFieldMap;

    private final ArrayList<CloudTile> chargedClouds;

    private final Map<String, Board> boardMap;

    private final String currentPlayer;

    private final ArrayList<ExpertCard> experts;

    private final int numOfCoins;

    public WorldChangeMessage(Map<Integer, IsladNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        super(WORLD_CHANGE, "Server");
        this.gameFieldMap = gameFieldMap;
        this.chargedClouds = chargedClouds;
        this.boardMap = boardMap;
        this.currentPlayer = currentPlayer;
        this.experts = experts;
        this.numOfCoins = numOfCoins;
    }

    public Map<Integer, IsladNode> getGameFieldMap() {
        return gameFieldMap;
    }

    public ArrayList<CloudTile> getChargedClouds() {
        return chargedClouds;
    }

    public Map<String, Board> getBoardMap() {
        return boardMap;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<ExpertCard> getExperts() {
        return experts;
    }

    public int getNumOfCoins() {
        return numOfCoins;
    }
}
