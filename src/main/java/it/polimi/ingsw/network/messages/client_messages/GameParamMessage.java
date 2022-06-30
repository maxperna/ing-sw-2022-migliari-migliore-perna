package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Class GameParamMessage, the client sends this message to create a game.
 * It contains the parameters to create a Game.
 *
 * @author Miglia
 */
public class GameParamMessage extends Message {

    private final int numOfPlayers;
    private final boolean expertMode;

    public GameParamMessage(String senderPlayer, int numOfPlayers, boolean expertMode) {
        super(MessageType.GAME_PARAM, senderPlayer);
        this.numOfPlayers = numOfPlayers;
        this.expertMode = expertMode;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
