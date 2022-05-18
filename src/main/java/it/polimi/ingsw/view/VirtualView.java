package it.polimi.ingsw.view;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
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
     * @param clientHandler, used to communicate with the client
     */
    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void printText(String text) {
        System.out.println(text);
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
    public void showCurrentPlayer(String currentPlayer) {
        clientHandler.sendMessage(new CurrentPlayerMessage(currentPlayer));
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
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void disconnect() {
        clientHandler.disconnect();
    }
    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
