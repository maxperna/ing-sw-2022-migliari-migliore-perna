package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.Map;

public interface View{

    void start();

    void askPlayerNickname();

    /**Method to ask the gameParam to the first player. must be done before logging others players
     */
    void askGameParam();

    /**Method used to make the players choose the towerColor and the deckType,
     *each time the parameters get updated with the remaining objects.
     * @param remainingTowers list of remaining available towerColor
     * @param remainingDecks list of remaining available deckType
      */
    void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);

    /**Method that shows the initialized player, all the towers set and the students present in the entrance hall
     * @param numberOfTowers starting number of towers
     * @param entranceHall array with all the students
     */
    void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall); //probably unnecessary

    /**Method to show the entire game field, contains a map with all the nodes and their ID as key,
     * every player receives a gameField map at the beginning of the game.
     * @param gameFieldMap map with the gameField
     */
    void showGameField(Map<Integer, Node> gameFieldMap);

    /**Method to show all the clouds, every player receives newClouds at the beginning of the game.
     * @param newClouds arrayList that contains all the clouds in the game
     */
    void showClouds(ArrayList<CloudTile> newClouds);

    /**Method to show the currentPlayer, only one player receives a message that notifies his turn
     * @param currentPlayer nickName of the player that will play
     */
    void showCurrentPlayer(String currentPlayer, GameState currentState);

    /**Each time a teacher changes every player receives his personal teacherList.
     * @param teacherList contains the teacher of the player
     */
    void updateTeachers(Map<Color, Boolean> teacherList);

    /**Each time a node changes every player receives the updated node.
     * @param updatedNode node with updated attributes
     */
    void updateNode(Node updatedNode);   //we will use showGameField

    /**Show a generic message to the players.
     * E.G. the preparation phase ends, all the players receive a "Action phase started" message
     * @param genericMessage contains message
     */
    void showGenericMessage(String genericMessage);

    /**Each time the number of coins of a player changes each player receives an updated number of coins.
     * @param player is the player that modified his number of coins
     * @param numOfCoin is the new value of coin of the player
     */
    void newCoin(String player, int numOfCoin);

    /**
     * method that let the player choose which board will be checked
     * @param boardMap is a map containing all the boards in game
     */
    void showBoard (Map<String, Board> boardMap);

    /**
     * method used to print a specific board
     * @param board
     */
    void printBoard(Board board, String player);

    /**
     * method used to print the last assistant card used
     * @param mapCard is a map containing the last used cards
     */
    void showLastUsedCard(Map<String, AssistantCard> mapCard);

    void showWinner(String winner);

    void showError(String errorMessage, ErrorType errorType);

    void disconnect();

    void showAssistant(ArrayList<AssistantCard> deck);

    void connectionRequest();

    void showExpertCards(ArrayList<ExpertCard> availableExpertCards, ArrayList<ExpertCard> allExpertCards);

    /**
     * method used to choose which student move and the destination
     * @param students is the arraylist of available students
     * @param islands is the actual number of islands
     */
    void selectStudent(ArrayList<Color> students, int islands);

    void ActionPhaseTurn(Boolean expert);

    void moveMotherNature();

    void chooseCloudTile(int cloudID);

    void sendNumberOfPlayers(int numberOfPlayers);

    void playExpertType2(int cardID, Expert9 expert9);

    void playExpertType2(int cardID, Expert11 expert);

    void playExpertType2(int cardID, Expert12 expert);

    void playExpertType3(int cardID, Expert7 expert, ArrayList<Color> hallStudents);

    void playExpertType3(int cardID, Expert10 expert, ArrayList<Color> hallStudents, Map<Color, Integer> diningStudents);

    void playExpertType4(int cardID, Expert3 expert);

    void playExpertType5(int cardID, Expert1 expert);

    void playExpertType5(int cardID, Expert5 expert);

    void chooseAction();

}
