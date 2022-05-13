package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.UUID;

public class FirstLoginMessage extends Message {

    UUID identifier = UUID.randomUUID();
    private final String gameMode;
    private final boolean expertMode;


    public FirstLoginMessage(String senderPlayer, String gameMode, boolean expertMode) {
        super(MessageType.FIRST_LOGIN, senderPlayer);
        this.gameMode = gameMode;
        this.expertMode = expertMode;

    }

    public String getGameMode() {
        return gameMode;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

}
