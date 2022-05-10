package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class FirstLoginMessage extends Message {

    private final String gameMode;
    private final boolean expertMode;
    UUID identifier = UUID.randomUUID();

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
