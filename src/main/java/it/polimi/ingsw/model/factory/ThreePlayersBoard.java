package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.factory.BoardCreator;

import java.util.ArrayList;

public class ThreePlayersBoard implements BoardCreator {

    private static final int maxStudentHall = 9;
    private static final int maxTowers = 6;

    @Override
    public Board newBoard(TowerColor towerColor) {
        Board board = Board.createBoard(maxStudentHall, maxTowers);
        board.setStudentsOutside(Pouch.getInstance().randomDraw(maxStudentHall));

        ArrayList<Tower> listOfTowers = new ArrayList<>();
        for(int i = 0; i < maxTowers; i++)
            listOfTowers.add(new Tower(towerColor));
        board.setTowers(listOfTowers);

        System.out.println("Created 3 players Board");
        return board;
    }
}
