package it.polimi.ingsw.view;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;
import java.util.Map;

public interface View {

    void printText(String text);

    void askGameParam();

    void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall);
    void showGameField(Map<Integer, Node> gameFieldMap);
    void showClouds(ArrayList<CloudTile> newClouds);
    void showCurrentPlayer(String currentPlayer);
    void updateTeachers(Map<Color, Boolean> teacherList);
    void updateNode(Node updatedNode);
    void showGenericMessage(String genericMessage);

    void disconnect();
}
