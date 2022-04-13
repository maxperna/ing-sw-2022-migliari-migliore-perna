package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.GameField;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface Strategy {

    ArrayList <Player> createPlayers(UUID gameID);

    GameField createField(UUID gameID);
}
