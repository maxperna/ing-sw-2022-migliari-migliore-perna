package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.requests.PlayersNumberRequest;
import it.polimi.ingsw.network.server.ClientHandler;

import java.util.ArrayList;

public class VirtualView implements View {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void printText(String text) {
        System.out.println(text);
    }
    @Override
    public void askNumberOfPlayers() {
        clientHandler.sendMessage(new PlayersNumberRequest());
    }
    @Override
    public void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {

    }
    @Override
    public void disconnect() {
        clientHandler.disconnect();
    }


}
