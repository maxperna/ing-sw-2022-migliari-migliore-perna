package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.factory.BoardCreator;

public class TwoPlayersBoard implements BoardCreator {


    @Override
    public void newBoard() {
        System.out.println("Created 2 players Board");
    }
}
