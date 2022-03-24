package it.polimi.ingsw.model.factory;

public class ThreePlayersField implements GameFieldCreator {

    @Override
    public void newField() {
        System.out.println("Created 3 players Field");
    }
}
