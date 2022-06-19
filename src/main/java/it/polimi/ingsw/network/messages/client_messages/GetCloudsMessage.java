package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class GetCloudsMessage extends Message {

    private final int cloudID;

    public GetCloudsMessage(String senderPlayer, int cloudID) {

        super(MessageType.GET_CLOUD, senderPlayer);
        this.cloudID = cloudID;
    }

    public int getCloudID() {
        return cloudID;
    }
}
