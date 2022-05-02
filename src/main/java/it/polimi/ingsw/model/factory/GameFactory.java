package it.polimi.ingsw.model.factory;


import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class GameFactory, it allows selecting which type of logic use to create the components of the game
 * @author Miglia
 */
public class GameFactory {

    /**
     * createPlayers
     * selects the method to implement in PlayerCreator
     * @param selector used to select the set of rules to use
     * @return a factory method of the chosen type
     */
    public PlayerCreator createPlayers(String selector){
        if (selector == null || selector.isEmpty())
            return null;
        switch (selector) {
            case "TwoPlayers":
                return new TwoPlayers();
            case "ThreePlayers":
                return new ThreePlayers();
            case "FourPlayers":
                return new FourPlayer();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }

    /**
     * createField
     * selects the method to implement in GameFieldCreator
     * @param selector used to select the set of rules to use
     * @return a factory method of the chosen type
     */
    public GameFieldCreator createField(String selector){
        if (selector == null || selector.isEmpty())
            return null;
        switch (selector) {
            case "TwoPlayers":
                return new TwoPlayersField();
            case "ThreePlayers":
                return new ThreePlayersField();
            case "FourPlayers":
                return new FourPlayersFIeld();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }

    public static ArrayList <Color> drawFromPool(int arrayListLength, ArrayList<Color> arrayList) throws NotEnoughElements {


        if (arrayList.isEmpty())
            return null;

        if (arrayListLength > arrayList.size())
            throw new NotEnoughElements();

        ArrayList<Color> randomDraw = new ArrayList<Color>();

        Collections.shuffle(arrayList);
        for (int i = 0; i < arrayListLength; i++) {
            randomDraw.add(arrayList.remove(i)) ;
        }

        return randomDraw;
    }
}

