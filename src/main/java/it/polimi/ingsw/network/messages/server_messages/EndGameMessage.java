package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class EndGameMessage extends Message {

    private String winner;

    public EndGameMessage(String winner) {
        super(MessageType.END_GAME, "Server");
    }

    public String getWinner() {
        return winner;
    }
}
