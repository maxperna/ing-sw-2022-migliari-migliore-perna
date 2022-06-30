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

    /**
     * Method used to print the winning player
     * @param winner winning player's nickname
     */
    void showWinner(String winner);

    /**
     * method used to print an error message
     * @param errorMessage message that will be printed
     * @param errorType type of the error
     */
    void showError(String errorMessage, ErrorType errorType);

    /**
     * Method used to handle the disconnection
     */
    void disconnect();

    /**
     * Method used to print the assistant cards
     * @param deck is the arraylist of available cards
     */
    void showAssistant(ArrayList<AssistantCard> deck);

    /**
     * method used to manage the action phase
     * @param expert indicates if expert mode is on
     */
    void actionPhaseTurn(Boolean expert);

    /**
     * Method used to handle the connection request
     */
    void connectionRequest();

    /**
     * Method used to ask the player all the parameters required to play expert 9
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType2(int cardID, Expert9 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 11
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType2(int cardID, Expert11 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 12
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType2(int cardID, Expert12 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 7
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType3(int cardID, Expert7 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 10
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType3(int cardID, Expert10 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 3
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType4(int cardID, Expert3 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 1
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType5(int cardID, Expert1 expert);

    /**
     * Method used to ask the player all the parameters required to play expert 5
     * @param cardID is the id [0,1,2] of the card
     * @param expert is the expert card played
     */
    void playExpertType5(int cardID, Expert5 expert);

    /**
     * Method used to ask the player to choose between checking other boards or playing an assistant card
     */
    void chooseAction();

    /**
     * Method used to ask the player the number of moves that mother nature will perform
     */
    void moveMotherNature();

    /**
     * Method used to print all the game field structure on the cli
     * @param gameFieldMap is a map containing all the islands
     * @param chargedClouds is an arraylist of clouds
     * @param boardMap is a map containing all the boards
     * @param currentPlayer is the nickname of the current player
     * @param experts is an arraylist fo all the expert cards available during the game
     * @param numOfCoins is an int showing the coins owned by the player
     * @param nick is the nickname of the player
     */
    void worldUpdate(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String nick, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins);

    /**
     * Method used to ask the player which cloud he wants to take the students from
     * @param cloudID is the number of clouds
     */
    void chooseCloudTile(int cloudID);

    /**
     * Method used to print all the information about expert cards
     * @param allExpertCards all 3 expert cards available
     * @param numberOfCoins is the number of available coins
     */
    void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins);

    /**
     * Method used to ask the player to choose a student based on a list of available students
     * @param availableStudents students available
     * @param movementType destination of the movement
     * @param gameFieldSize island list dimension
     */
    void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize);

    /**
     * Method used to ask the player which expert card he wants to play
     */
    void chooseExpertCard();

    /**
     * Method used to ask the player if he wants to play an expert or move mother nature
     */
    void moveMNplusExpert(boolean expertPlayed);

    void clear();

    void startGame();

    void availableAction(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed);

    void sendNumberOfPlayers(int num_of_players, boolean expertMode);
}
