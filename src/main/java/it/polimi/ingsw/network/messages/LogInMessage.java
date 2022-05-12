package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.UUID;

public class LogInMessage extends  Message{

    UUID identifier = UUID.randomUUID();
    private final TowerColor chosenTowerColor;
    private final DeckType chosenDeckType;

    public LogInMessage(String senderPlayer, TowerColor chosenTowerColor, DeckType chosenDeckType) {
        super(MessageType.LOGIN, senderPlayer);
        this.chosenTowerColor = chosenTowerColor;
        this.chosenDeckType = chosenDeckType;
    }

    public TowerColor getChosenTowerColor() {
        return chosenTowerColor;
    }

    public DeckType getChosenDeckType() {
        return chosenDeckType;
    }
}
