package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.Map;

public interface View {

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

    /**Method to show all the experts, every player receives expertIDLists at the beginning of the game.
     * @param expertIDList arrayList that contains all the experts in the game
     */
    void showExpertCards(ArrayList<ExpertID> expertIDList);

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
    void printBoard(Board board);

    void showPlayedAssistantCard (Map<String, AssistantCard> lastCardMap); //simplified by showLastUsedCard

    void showWinner(String winner);

    void showError(String errorMessage);

    void showExpertID(ArrayList<ExpertID> expertID);

    void showExpertCard(ArrayList<ExpertCard> expertCard);

    void disconnect();

    void showAssistant(ArrayList<AssistantCard> deck);

    @TestOnly
    void ActionPhaseTurn();

    void connectionRequest();

    /**
     * method used to print the last assistant card used
     * @param card is the last assistant card used
     * @param playerName is the player who played that card
     */
    void showLastUsedCard(AssistantCard card, String playerName);

    /**
     * method used to choose which student move and the destination
     * @param students is the arraylist of available students
     * @param islands is the actual number of islands
     */
    void selectStudent(ArrayList<Color> students, int islands);

    void chooseAction();

    void moveMotherNature();

}
