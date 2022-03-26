package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;

public interface BoardCreator {
    Board newBoard(TowerColor towerColor);
}
