package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.factory.BoardCreator;

public class ThreePlayersBoard implements BoardCreator {

    @Override
    public void newBoard() {
        System.out.println("Created 3 players Board");
    }
}
