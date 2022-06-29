package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.Map;

public interface View {

    void start();

    void askPlayerNickname();

    /**
     * Method to ask the gameParam to the first player. must be done before logging others players
     */
    void askGameParam();

    /**
     * Method used to make the players choose the towerColor and the deckType,
     * each time the parameters get updated with the remaining objects.
     *
     * @param remainingTowers list of remaining available towerColor
     * @param remainingDecks  list of remaining available deckType
     */
    void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks);


    /**
     * Method to show the entire game field, contains a map with all the nodes and their ID as key,
     * every player receives a gameField map at the beginning of the game.
     *
     * @param gameFieldMap map with the gameField
     */
    void showGameField(Map<Integer, IslandNode> gameFieldMap);

    /**
     * Method to show all the clouds, every player receives newClouds at the beginning of the game.
     *
     * @param newClouds arrayList that contains all the clouds in the game
     */
    void showClouds(ArrayList<CloudTile> newClouds);

    /**
     * Method to show the currentPlayer, only one player receives a message that notifies his turn
     *
     * @param currentPlayer nickName of the player that will play
     */
    void showCurrentPlayer(String currentPlayer, GameState currentState);


    /**
     * Shows a generic message to the players.
     * E.G. the preparation phase ends, all the players receive an "Action phase started" message
     *
     * @param genericMessage contains message
     */
    void showGenericMessage(String genericMessage);

    /**
     * Each time the number of coins of a player changes each player receives an updated number of coins.
     *
     * @param player    is the player that modified his number of coins
     * @param numOfCoin is the new value of coin of the player
     */
    void newCoin(String player, int numOfCoin);

    /**
     * method that let the player choose which board will be checked
     *
     * @param boardMap is a map containing all the boards in game
     */
    void showBoard(Map<String, Board> boardMap);

    /**
     * method used to print a specific board
     *
     * @param board is the board that will be printed
     */
    void printBoard(Board board, String player);

    /**
     * method used to print the last assistant card used
     *
     * @param mapCard is a map containing the last used cards
     */
    void showLastUsedCard(Map<String, AssistantCard> mapCard);

    void showWinner(String winner);

    void showError(String errorMessage, ErrorType errorType);

    void disconnect();

    void showAssistant(ArrayList<AssistantCard> deck);

    void ActionPhaseTurn(Boolean expert);

    void connectionRequest();

    void worldUpdate(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap,String nick, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins);

    void playExpertType2(int cardID, Expert9 expert);

    void playExpertType2(int cardID, Expert11 expert);

    void playExpertType2(int cardID, Expert12 expert);

    void playExpertType3(int cardID, Expert7 expert);

    void playExpertType3(int cardID, Expert10 expert);

    void playExpertType4(int cardID, Expert3 expert);

    void playExpertType5(int cardID, Expert1 expert);

    void playExpertType5(int cardID, Expert5 expert);

    void chooseAction(boolean expertMode);

    void moveMotherNature();

    void chooseCloudTile(int cloudID);

    void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins);

    void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize);

    void chooseExpertCard();

    void playExpertChoice();

    void clear();

    void startGame();
}
