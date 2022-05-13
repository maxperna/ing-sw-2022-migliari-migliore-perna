package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.UUID;

public class CreatePlayerMessage extends  Message{

    UUID identifier = UUID.randomUUID();
    private final TowerColor chosenTowerColor;
    private final DeckType chosenDeckType;

    public CreatePlayerMessage(String senderPlayer, TowerColor chosenTowerColor, DeckType chosenDeckType) {
        super(MessageType.PLAYER_CREATION, senderPlayer);
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
