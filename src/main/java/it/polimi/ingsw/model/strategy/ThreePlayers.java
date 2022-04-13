package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.UUID;

public class ThreePlayers implements Strategy{

    private static final int maxStudentHall = 9;
    private static final int maxTowers = 6;
    private static final int numberOfPlayers = 3;

    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers,maxTowers,maxStudentHall);
    }
}
