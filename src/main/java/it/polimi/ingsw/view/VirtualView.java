package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.GameParamRequest;
import it.polimi.ingsw.network.messages.server_messages.*;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class VirtualView, one for each player
 *
 * @author Miglia
 */
public class VirtualView implements View {

    private final ClientHandler clientHandler;

    /**
     * Constructor, creates a Virtual View
     *
     * @param clientHandler, used to communicate with the client
     */
    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void start() {
    }

    @Override
    public void askPlayerNickname() {
        clientHandler.sendMessage(new NicknameRequest());
    }

    /**
     * method that asks the first player the game parameters
     */
    @Override
    public void askGameParam() {
        clientHandler.sendMessage(new GameParamRequest());
    }

    /**
     * method that tells which type of towers and decks haven't been selected yet
     *
     * @param remainingTowers list of the remaining towerColors
     * @param remainingDecks  list of the remaining deckTypes
     */
    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        clientHandler.sendMessage(new RemainingItemReply(remainingTowers, remainingDecks));
    }


    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        clientHandler.sendMessage(new UpdateCloudsMessage(newClouds));
    }

    @Override
    public void showGameField(Map<Integer, IslandNode> gameFieldMap) {
        clientHandler.sendMessage(new GameFieldMessage(gameFieldMap));
    }

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {
        clientHandler.sendMessage(new CurrentPlayerMessage(currentPlayer, currentState));
    }

    @Override
    public void showExpertCards(ArrayList<ExpertCard> expertList, int numberOfCoins) {
        clientHandler.sendMessage(new ExpertCardReply(expertList, numberOfCoins));
    }

    @Override
    public void chooseExpertCard() {

    }

    public void expertModeControl(boolean setExpertMode) {
        clientHandler.sendMessage(new ExpertModeControlMessage(setExpertMode));
    }

    @Override
    public void playExpertChoice() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void newCoin(String player, int numOfCoin) {
        clientHandler.sendMessage(new UpdateCoin(player, numOfCoin));
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void showWinner(String winner) {
        clientHandler.sendMessage(new EndGameMessage(winner));
    }

    @Override
    public void showError(String errorMessage, ErrorType errorType) {
        clientHandler.sendMessage(new ErrorMessage(errorMessage, errorType));
    }


    @Override
    public void disconnect() {
        clientHandler.disconnect();
    }

    @Override
    public void showAssistant(ArrayList<AssistantCard> cards) {
        clientHandler.sendMessage(new AssistantCardsMessage(cards));
    }

    @Override
    public void showLastUsedCard(Map<String, AssistantCard> lastCardMap) {
        clientHandler.sendMessage(new LastCardMessage(lastCardMap));
    }

    @Override
    public void showBoard(Map<String, Board> boardMap) {
        clientHandler.sendMessage(new BoardInfoMessage(boardMap));
    }

    @Override
    public void printBoard(Board board, String nickname) {

    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @Override
    public void ActionPhaseTurn(Boolean expert) {
    }


    @Override
    public void connectionRequest() {

    }

    @Override
    public void playExpertType2(int cardID, Expert9 expert) {

    }

    @Override
    public void playExpertType2(int cardID, Expert11 expert) {

    }

    @Override
    public void playExpertType2(int cardID, Expert12 expert) {

    }

    @Override
    public void playExpertType3(int cardID, Expert7 expert) {

    }

    @Override
    public void playExpertType3(int cardID, Expert10 expert) {

    }

    @Override
    public void playExpertType4(int cardID, Expert3 expert) {

    }

    @Override
    public void playExpertType5(int cardID, Expert1 expert) {

    }

    @Override
    public void playExpertType5(int cardID, Expert5 expert) {

    }

    @Override
    public void chooseAction(boolean expertMode) {
    }

    @Override
    public void moveMotherNature() {
    }

    @Override
    public void worldUpdate(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String nick,String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        clientHandler.sendMessage(new WorldChangeMessage(gameFieldMap, chargedClouds, boardMap, currentPlayer, experts, numOfCoins));

    }

    public void chooseCloudTile(int cloudID) {
    }


    public void setExpertMode(boolean expertMode) {
        clientHandler.sendMessage(new ExpertModeNotify(expertMode));
    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        clientHandler.sendMessage(new AvailableStudentsReply(availableStudents, movementType, gameFieldSize));
    }

    @Override
    public void startGame() {
        clientHandler.sendMessage(new StartGameMessage());
    }

    @Override
    public void availableAction(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed) {
        clientHandler.sendMessage(new AvailableActionMessage(allStudentsMoved, motherNatureMoved, expertPlayed));
    }

    public void sendNumberOfPlayers(int num_of_players) {
    }

    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {
        clientHandler.sendMessage(new PlayerInitMessage(numberOfTowers, entranceHall));
    }
}
