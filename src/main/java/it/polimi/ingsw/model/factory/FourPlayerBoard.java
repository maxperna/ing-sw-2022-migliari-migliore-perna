package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.factory.BoardCreator;

public class FourPlayerBoard implements BoardCreator {

    @Override
    public Board newBoard() {
        Board board = new Board();
        System.out.println("Created 4 players Board");
        return board;
    }
}
