package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;

public interface View {

    void printText(String text);

    void askGameParam();

    void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    void disconnect();
}
