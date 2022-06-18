package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
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

    @Override
    public void start() {
    }

    /**
     * Constructor, creates a Virtual View
     * @param clientHandler, used to communicate with the client
     */
    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
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
     * @param remainingTowers list of the remaining towerColors
     * @param remainingDecks list of the remaining deckTypes
     */
    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        clientHandler.sendMessage(new RemainingItemReply(remainingTowers, remainingDecks));
    }

    @Override
    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {
        clientHandler.sendMessage(new PlayerInitMessage(numberOfTowers, entranceHall));
    }

    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        clientHandler.sendMessage(new UpdateCloudsMessage(newClouds));
    }

    @Override
    public void showGameField(Map<Integer, Node> gameFieldMap) {
        clientHandler.sendMessage(new GameFieldMessage(gameFieldMap));
    }

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {
        clientHandler.sendMessage(new CurrentPlayerMessage(currentPlayer, currentState));
    }

    @Override
    public void showExpertCards(ArrayList<ExpertID> expertIDList) {
        clientHandler.sendMessage(new ExpertCardReply(expertIDList));
    }
    @Override
    public void updateTeachers(Map<Color, Boolean> teacherList) {
        clientHandler.sendMessage(new TeacherListMessage(teacherList));
    }

    @Override
    public void updateNode(Node updatedNode) {
        clientHandler.sendMessage(new UpdateNodeMessage(updatedNode));
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
    public void showLastUsedCard (Map<String, AssistantCard> lastCardMap) {
        clientHandler.sendMessage(new LastCardMessage(lastCardMap));
    }

    @Override
    public void showBoard (Map<String, Board> boardMap) {
        clientHandler.sendMessage(new BoardInfoMessage(boardMap));
    }

    @Override
    public void printBoard(Board board, String nickname) {

    }

    @Override
    public void showExpertID(ArrayList<ExpertID> expertID){
        clientHandler.sendMessage(new ExpertCardReply(expertID));
    }

    @Override
    public void showExpertCard(ArrayList<ExpertCard> expertCard) {
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void ActionPhaseTurn(){}

    @Override
    public void connectionRequest() {

    }

    @Override
    public void selectStudent(ArrayList<Color> students, int islands) {

    }

    public void chooseAction(){}

    @Override
    public void moveMotherNature() {

    }

    public void worldUpdate(Map<Integer, Node> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String currentPlayer) {
        clientHandler.sendMessage(new WorldChangeMessage(gameFieldMap, chargedClouds, boardMap, currentPlayer));
    }

    public void chooseCloudTile(int cloudID){}

    @Override
    public void sendNumberOfPlayers(int numberOfPlayers) {
        clientHandler.sendMessage((new NumberOfPlayersMessage(numberOfPlayers)));
    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        clientHandler.sendMessage(new AvailableStudentsReply(availableStudents, movementType, gameFieldSize));
    }

}
