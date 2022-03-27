package it.polimi.ingsw.model.factory;

public class TwoPlayersField implements GameFieldCreator{

    @Override
    public void newField() {

        System.out.println("Created 2 players Field\n");
    }
}
