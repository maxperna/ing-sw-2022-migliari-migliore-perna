package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Popup;

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
        SceneController.setCurrentController(pwc);
        Platform.runLater(() -> SceneController.changeRoot(list, pwc, "PlayerView.fxml"));
        //Platform.runLater(SceneController::setFullScreen);
    }

    @Override
    public void availableAction(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed) {

    }

    @Override
    public void showGameField(Map<Integer, IslandNode> gameFieldMap) {
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
    public void showGenericMessage(String genericMessage) {
        Platform.runLater(()->SceneController.showMessage(Alert.AlertType.INFORMATION,genericMessage));
    }

    @Override
    public void newCoin(String player, int numOfCoin) {

    }

    @Override
    public void showBoard(Map<String, Board> boardMap) {

        BoardInfoSceneController boardInfoSceneController = new BoardInfoSceneController(boardMap);
        try {
            Platform.runLater(() -> SceneController.showNewStage(list, boardInfoSceneController, PopUpType.DEFAULT, "BoardInfoScene.fxml", "BoardInfo"));
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
            Platform.runLater(() -> SceneController.showNewStage(list, assistantCardsController, PopUpType.DEFAULT, "AssistantCardsScene.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Enable student movement*/
    @Override
    public void ActionPhaseTurn(Boolean bool) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()->pwc.switchStudentMovement());
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
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.setPreparationPhaseChoiceBox(expertMode));
    }

    @Override
    public void moveMotherNature() {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()-> pwc.switchMN());
    }

    public void chooseCloudTile(int cloudID) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()->pwc.changeCloudStatus());
    }


    @Override
    public void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins) {
        ExpertCardSceneController exsx = new ExpertCardSceneController();
        exsx.setExpertCards(allExpertCards);
        Platform.runLater(()->SceneController.showNewStage(list,exsx, PopUpType.EXPERT, "ExpertsCardScene.fxml","Experts"));
    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        //Utile solo per CLI
    }

    @Override
    public void chooseExpertCard() {

    }

    @Override
    public void playExpertChoice() {

    }

    @Override
    public void clear() {

    }

    public void worldUpdate(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap,String nick, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.updateGameField(gameFieldMap, chargedClouds, boardMap.get(nick),currentPlayer, experts, numOfCoins));


    }

    private PlayerViewController getPWC() {
        PlayerViewController pwc;
        pwc = (PlayerViewController) SceneController.currentController;
        return pwc;
    }

}