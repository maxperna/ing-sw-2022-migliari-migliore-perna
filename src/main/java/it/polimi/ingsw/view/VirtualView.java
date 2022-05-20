package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.client_messages.GameParamRequest;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void sendGameParam() {
        clientHandler.sendMessage(new GameParamRequest());
    }

    @Override
    public void getServerInfo() {

    }

    @Override
    public void askPlayerNickname() {

    }

    @Override
    public void connectionRequest(HashMap<String, String> connectionInfo) {

    }

    @Override
    public void sendSelectedID(ArrayList<Integer> ID) {

    }

    @Override
    public void selectStudent(ArrayList<Color> students) {

    }

    @Override
    public void catchAction(Message receivedMessage) {

    }

    @Override
    public void chooseTowerColor(ArrayList<TowerColor> availableColors) {

    }

    @Override
    public void chooseCardDeck(ArrayList<DeckType> decksAvailable) {

    }

    @Override
    public void chooseDestination() {

    }

    @Override
    public void getPlayerInfo(ArrayList<String> players) {

    }

    /**
     * method that tells which type of towers and decks haven't been selected yet
     * @param remainingTowers list of the remaining towerColors
     * @param remainingDecks list of the remaining deckTypes
     */
    @Override
    public void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {

    }
    @Override
    public void disconnect() {
        clientHandler.disconnect();
    }
    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
