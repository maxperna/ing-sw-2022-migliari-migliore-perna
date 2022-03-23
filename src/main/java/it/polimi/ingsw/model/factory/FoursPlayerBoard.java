package it.polimi.ingsw.model.factory;

public class FoursPlayerBoard implements BoardCreator {

    @Override
    public void newBoard() {
        System.out.println("Created 4 players Board");
    }
}
