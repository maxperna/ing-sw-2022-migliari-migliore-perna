package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.UUID;

public class ThreePlayers implements Strategy{

    static final int maxStudentHall = 9;
    static final int maxTowers = 6;
    static final int numberOfPlayers = 3;

    @Override
    public Game generateGame() {

        return new Game(numberOfPlayers,maxTowers,maxStudentHall);
    }
}
