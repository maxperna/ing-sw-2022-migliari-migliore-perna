package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;

public interface View {

    void printText(String text);

    void askNumberOfPlayers();

    void askParamGame(String senderPlayer, String gameMode, boolean expertMode);

    void remainingTowerAndDeck(String nickname, ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    void disconnect();
}
