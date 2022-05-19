package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class UpdateCoin extends Message {

    private final String nickName;
    private final int coinValue;
    public UpdateCoin(String nickName, int coinValue) {
        super(MessageType.UPDATE_COIN, "Server");

        this.nickName = nickName;
        this.coinValue = coinValue;
    }

    public String getNickName() {
        return nickName;
    }

    public int getCoinValue() {
        return coinValue;
    }
}
