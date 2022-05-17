package it.polimi.ingsw.view;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface View {

    void printText(String text);

    void askGameParam();

    void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall);
    void showGameField(Map<Integer, Node> gameFieldMap);
    void showChargedClouds(Map<Integer, ArrayList<Color>> chargedClouds);

    void showCurrentPlayer(String currentPlayer);

    void updateTeachers(List<Color> teacherList);
    void showGenericMessage(String genericMessage);

    void disconnect();
}
