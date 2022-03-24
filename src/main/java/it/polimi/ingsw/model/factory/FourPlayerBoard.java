package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.factory.BoardCreator;

public class FourPlayerBoard implements BoardCreator {

    @Override
    public void newBoard() {
        System.out.println("Created 4 players Board");
    }
}
