package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class PlayerInitMessage extends Message {

    private final int numberOfTowers;
    private final ArrayList<Color> entranceHall;

    public PlayerInitMessage(int numberOfTowers, ArrayList<Color> entranceHall) {

        super(MessageType.PLAYER_INIT, "Sono Beppe Sala");
        this.numberOfTowers = numberOfTowers;
        this.entranceHall = entranceHall;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public ArrayList<Color> getEntranceHall() {
        return entranceHall;
    }

}
