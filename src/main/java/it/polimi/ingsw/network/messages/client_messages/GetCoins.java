package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class GetCoins extends Message {
    public GetCoins(String sender) {
        super(MessageType.GET_COINS, sender);
    }
}
