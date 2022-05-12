package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.UUID;

public class FirstLoginMessage extends Message {

    private final TowerColor chosenTowerColor;
    private final String gameMode;
    private final boolean expertMode;
    private final DeckType chosenDeckType;
    UUID identifier = UUID.randomUUID();



    public FirstLoginMessage(String senderPlayer, String gameMode, boolean expertMode, TowerColor chosenTowerColor, DeckType chosenDeckType) {
        super(MessageType.FIRST_LOGIN, senderPlayer);
        this.gameMode = gameMode;
        this.expertMode = expertMode;
        this.chosenTowerColor = chosenTowerColor;
        this.chosenDeckType = chosenDeckType;
    }

    public String getGameMode() {
        return gameMode;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public TowerColor getChosenTowerColor() {
        return chosenTowerColor;
    }

    public DeckType getChosenDeckType() {
        return chosenDeckType;
    }
}
