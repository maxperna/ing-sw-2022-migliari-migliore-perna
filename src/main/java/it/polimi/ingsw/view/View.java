package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;

public interface View {

    void printText(String text);

    void sendGameParam();

    void getServerInfo();

    void askPlayerNickname();

    void connectionRequest(HashMap<String, String> connectionInfo);

    void sendSelectedID(ArrayList<Integer> ID);

    void selectStudent(ArrayList<Color> students);

    void catchAction(Message receivedMessage);

    void chooseTowerColor(ArrayList<TowerColor> availableColors);

    void chooseCardDeck(ArrayList<DeckType> decksAvailable);

    void chooseDestination();

    void getPlayerInfo(ArrayList<String> players);

    void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    void disconnect();


}
