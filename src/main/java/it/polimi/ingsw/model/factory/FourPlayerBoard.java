package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.factory.BoardCreator;

public class FourPlayerBoard implements BoardCreator {

    private static final int maxStudentHall = 7;
    private static final int maxTowers = 8;

    @Override
    public Board newBoard() {
        Board board = Board.createBoard(maxStudentHall, maxTowers);
        System.out.println("Created 4 players Board");
        return board;
    }
}
