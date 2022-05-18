package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.Map;

public class UpdateCloudsMessage extends Message {

    private final ArrayList<CloudTile> chargedClouds;

    public UpdateCloudsMessage(ArrayList<CloudTile> chargedClouds) {
        super(MessageType.CHARGECLOUD, "Server");
        this.chargedClouds = chargedClouds;
    }

    public ArrayList<CloudTile> getChargedClouds() {
        return chargedClouds;
    }
}
