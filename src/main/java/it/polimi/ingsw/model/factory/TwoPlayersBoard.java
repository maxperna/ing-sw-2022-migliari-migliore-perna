package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.factory.BoardCreator;

public class TwoPlayersBoard implements BoardCreator {


    @Override
    public Board newBoard() {
        Board board = new Board();
        System.out.println("Created 2 players Board");
        return board;
    }
}
