package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

public class ChargedCloudsMessage extends Message {

    private final HashMap<Integer, ArrayList<Color>> chargedClouds;

    public ChargedCloudsMessage(HashMap<Integer, ArrayList<Color>> chargedClouds) {
        super(MessageType.CHARGECLOUD, "Sono Beppe Sala");
        this.chargedClouds = chargedClouds;
    }

    public HashMap<Integer, ArrayList<Color>> getChargedClouds() {
        return chargedClouds;
    }
}
