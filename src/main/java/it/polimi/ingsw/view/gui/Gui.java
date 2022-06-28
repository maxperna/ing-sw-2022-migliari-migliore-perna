package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IsladNode;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.AssistantCardsController;
import it.polimi.ingsw.view.gui.scenes.BoardInfoSceneController;
import it.polimi.ingsw.view.gui.scenes.PlayerViewController;
import it.polimi.ingsw.view.gui.scenes.TowerDeckSelectionControllerGeneric;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Map;

public class Gui extends ViewSubject implements View {

    @Override
    public void start() {

    }

    @Override
    public void askPlayerNickname() {
        Platform.runLater(() -> SceneController.changeRoot(list, "NicknameInput.fxml"));
    }

    @Override
    public void askGameParam() {
        Platform.runLater(() -> SceneController.changeRoot(list, "GameParamScene.fxml"));
    }

    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        TowerDeckSelectionControllerGeneric towerDeckSelectionController = new TowerDeckSelectionControllerGeneric();
        towerDeckSelectionController.setRemainingTowers(remainingTowers);
        towerDeckSelectionController.setRemainingDecks(remainingDecks);

        Platform.runLater(() -> SceneController.changeRoot(list, towerDeckSelectionController, "TowerDeckSelection.fxml"));
    }

    public void startGame() {
        PlayerViewController pwc = new PlayerViewController();
        Platform.runLater(() -> SceneController.changeRoot(list, pwc, "PlayerView.fxml"));
        //Platform.runLater(SceneController::setFullScreen);
    }

    @Override
    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {

    }

    @Override
    public void showGameField(Map<Integer, IsladNode> gameFieldMap) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateIslands(gameFieldMap));
    }

    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateCloud(newClouds));
    }

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {

    }

    @Override
    public void updateTeachers(Map<Color, Boolean> teacherList) {

    }

    @Override
    public void updateNode(IsladNode updatedNode) {

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void newCoin(String player, int numOfCoin) {

    }

    @Override
    public void showBoard(Map<String, Board> boardMap) {

        BoardInfoSceneController boardInfoSceneController = new BoardInfoSceneController(boardMap);
        try {
            Platform.runLater(() -> SceneController.showNewStage(list, boardInfoSceneController, "BoardInfoScene.fxml", "BoardInfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printBoard(Board board, String player) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateBoard(board));
    }

    @Override
    public void showLastUsedCard(Map<String, AssistantCard> lastCard) {
        AssistantCardsController assistantCardsController = new AssistantCardsController();
        assistantCardsController.setLastCard(lastCard);
        SceneController.popUpController = assistantCardsController;
    }

    @Override
    public void showWinner(String winner) {

    }

    @Override
    public void showError(String errorMessage, ErrorType errorType) {

    }



    /*@Override
    public void showExpertCards(ArrayList<ExpertCard> expertCard) {
        PlayerViewController pwc =getPWC();
        Platform.runLater(()->pwc.setExp());
    }*/

    @Override
    public void disconnect() {

    }

    @Override
    public void showAssistant(ArrayList<AssistantCard> deck) {

        AssistantCardsController assistantCardsController = (AssistantCardsController) SceneController.popUpController;
        assistantCardsController.setDeck(deck);

        try {
            Platform.runLater(() -> SceneController.showNewStage(list, assistantCardsController, "AssistantCardsScene.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ActionPhaseTurn(Boolean bool) {

    }

    @Override
    public void ActionPhaseTurn() {

    }

    @Override
    public void connectionRequest() {

    }

    @Override
    public void selectStudent(ArrayList<Color> students, int islands) {

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
    public void chooseAction(/*boolean expert*/) {
        notifyListener(list -> list.chooseAction(2));
    }

    @Override
    public void moveMotherNature() {

    }

    public void chooseCloudTile(int cloudID) {
    }

    @Override
    public void sendNumberOfPlayers(int numberOfPlayers) {

    }

    @Override
    public void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins) {

    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {

    }

    @Override
    public void chooseExpertCard() {

    }

    @Override
    public void expertModeControl(boolean setExpertMode) {

    }

    @Override
    public void playExpertChoice() {

    }

    @Override
    public void worldUpdate(Map<Integer, IsladNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.updateGameField(gameFieldMap, chargedClouds, boardMap.get(currentPlayer), experts, numOfCoins));
    }

    private PlayerViewController getPWC() {
        PlayerViewController pwc;
        try {
            pwc = (PlayerViewController) SceneController.currentController;
        } catch (ClassCastException e) {
            pwc = new PlayerViewController();
            PlayerViewController finalPWC = pwc;
            Platform.runLater(() -> SceneController.changeRoot(list, finalPWC, "PlayerView.fxml"));
        }
        return pwc;
    }

    @Override
    public void showExpertCards(ArrayList<ExpertCard> expertCard) {

    }
}