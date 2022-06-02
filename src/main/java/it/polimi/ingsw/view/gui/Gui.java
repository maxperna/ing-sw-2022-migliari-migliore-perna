package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Map;

public class Gui implements View {

    @Override
    public void start() {

    }

    @Override
    public void askPlayerNickname() {

    }

    @Override
    public void askGameParam() {

    }

    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {

    }

    @Override
    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {

    }

    @Override
    public void showGameField(Map<Integer, Node> gameFieldMap) {

    }

    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {

    }

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {

    }

    @Override
    public void showExpertCards(ArrayList<ExpertID> expertIDList) {

    }

    @Override
    public void updateTeachers(Map<Color, Boolean> teacherList) {

    }

    @Override
    public void updateNode(Node updatedNode) {

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void newCoin(String player, int numOfCoin) {

    }

    @Override
    public void showBoard(Map<String, Board> boardMap) {

    }

    @Override
    public void printBoard(Board board) {

    }

    @Override
    public void showPlayedAssistantCard(Map<String, AssistantCard> lastCardMap) {

    }

    @Override
    public void showWinner(String winner) {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showExpertID(ArrayList<ExpertID> expertID) {

    }

    @Override
    public void showExpertCard(ArrayList<ExpertCard> expertCard) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void showAssistant(ArrayList<AssistantCard> deck) {

    }

    @Override
    public void startActionPhase() {

    }

    @Override
    public void connectionRequest() {

    }

    @Override
    public void showLastUsedCard(AssistantCard card, String playerName) {

    }

    @Override
    public void selectStudent(ArrayList<Color> students, int islands) {

    }

    @Override
    public void chooseAction() {

    }
}
