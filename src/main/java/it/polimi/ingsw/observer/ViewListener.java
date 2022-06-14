package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Extended interface of a Listener (old Observer) used to interact with the View, has to be implemented on every class that shares methods with the view, and on Client Controller to call the methods 1:1
 */
public interface ViewListener {

    void connectionRequest(HashMap<String,String> connectionInfo);

    void sendNickname(String nickname);

    void sendGameParam(int numOfPlayers, boolean expertMode);

    void chooseCloudTile(int ID);

    /**Method to get boards*/
    void getBoards();

    void moveStudentToIsland(Color student,int nodeID);

    void moveStudentToDinner(Color student);

    void getExpertsCard();

    void getGameField();

    void catchAction(Message receivedMessage);

    void chooseTowerColorAndDeck(TowerColor color, DeckType deck);

    void playAssistantCard(int playedCard);

    void chooseDestination(String destination);

    void cloudsRequest();

    void getPlayerInfo(String player);

    void moveMotherNature(int numberOfSteps);

    //EXPERT CARD METHODS

    void chooseAction(int finalChosenAction);

    void playExpertCard1(int cardID);

    void playExpertCard2(int cardID,int nodeID);

    void playExpertCard3(int nodeID,Color student,int cardID);

    void playExpertCard4(Color student,int cardID);

    void playExpertCard5(ArrayList<Color> student1, ArrayList<Color> student2,int cardID);

    void applyExpertEffect(int finalChosenExpert);

    void getCoins();
}
