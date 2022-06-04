package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class NumberOfPlayersMessage extends Message {

    private final int numberOfPlayers;

    public NumberOfPlayersMessage(int numberOfPlayers) {
        super(MessageType.NUMBER_PLAYERS, "Server");
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
