package it.polimi.ingsw.model.factory;

public class FourPlayersFIeld implements GameFieldCreator{
    @Override
    public void newField() {
        System.out.println("Created 4 players Field\n");
    }

}
