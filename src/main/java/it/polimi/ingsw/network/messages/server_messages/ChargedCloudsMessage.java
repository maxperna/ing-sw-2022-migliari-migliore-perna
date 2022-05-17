package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChargedCloudsMessage extends Message {

    private final Map<Integer, ArrayList<Color>> chargedClouds;

    public ChargedCloudsMessage(Map<Integer, ArrayList<Color>> chargedClouds) {
        super(MessageType.CHARGECLOUD, "Sono Beppe Sala");
        this.chargedClouds = chargedClouds;
    }

    public Map<Integer, ArrayList<Color>> getChargedClouds() {
        return chargedClouds;
    }
}
