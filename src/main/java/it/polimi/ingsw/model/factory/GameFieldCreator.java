package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.GameField;

import java.util.UUID;

public interface GameFieldCreator {
    GameField newField(UUID gameID);
}
