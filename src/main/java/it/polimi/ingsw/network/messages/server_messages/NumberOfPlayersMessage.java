package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class NumberOfPlayersMessage extends Message {

    private final int numberOfPlayers;
    private final boolean expertMode;

    public NumberOfPlayersMessage(int numberOfPlayers, boolean expertMode) {
        super(MessageType.NUMBER_PLAYERS, "Server");
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
