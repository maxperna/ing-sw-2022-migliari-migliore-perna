package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;

import java.util.HashMap;

/**
 * Extended interface of a Listener (old Observer) used to interact with the View, has to be implemented on every class that shares methods with the view, and on Client Controller to call the methods 1:1
 */
public interface ViewListener {

    void connectionRequest(HashMap<String,String> connectionInfo);

    void sendNickname(String nickname);

    void sendGameParam(int numOfPlayers, boolean expertMode);

    void sendSelectedID(int ID);

    void moveStudentToIsland(Color student,int nodeID);

    void moveStudentToDinner(Color student);

    void catchAction(Message receivedMessage);

    void chooseTowerColorAndDeck(TowerColor color, DeckType deck);

    void chooseDestination(String destination);

    void getPlayerInfo(String player);
}
