package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;

import java.io.Serializable;

public class IntHolder implements Serializable {

    private int value;

    IntHolder(int value) {
        this.value = value;
    }

    public void increaseValue() {
        value=value+1;
    }

    public void decreaseValue() throws EndGameException {
        if(value < 0)
            throw new IllegalStateException("Valore torri Negativo");

        value = value-1;

        if(value == 0)
            throw new EndGameException();
    }

    public int getValue() {
        return value;
    }
}
