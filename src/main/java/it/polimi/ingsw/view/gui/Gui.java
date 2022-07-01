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
    public void sendNumberOfPlayers(int num_of_players, boolean expertMode) {

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
            Platform.runLater(() -> SceneController.showNewPopUp(list, boardInfoSceneController, ChangeType.DEFAULT, "BoardInfoScene.fxml", "BoardInfo"));
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
            Platform.runLater(() -> SceneController.showNewPopUp(list, assistantCardsController, ChangeType.DEFAULT, "AssistantCardsScene.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Enable student movement*/
    @Override
    public void actionPhaseTurn(Boolean expertPlayed, boolean studentMove) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()->pwc.switchStudentMovementStatus(studentMove));
    }

    @Override
    public void connectionRequest() {

    }


    @Override
    public void playExpert9(int cardID, Expert9 expert) {
        Expert9_12_SceneController exp9 = new Expert9_12_SceneController(cardID);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp9,ChangeType.EXPERT,"Expert9.fxml"));
    }

    @Override
    public void playExpert11(int cardID, Expert11 expert) {
        Expert11SceneController exp11 = new Expert11SceneController(cardID,expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp11,ChangeType.EXPERT,"Expert11.fxml"));
    }

    @Override
    public void playExpert12(int cardID, Expert12 expert) {
        Expert9_12_SceneController exp9 = new Expert9_12_SceneController(cardID);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp9,ChangeType.EXPERT,"Expert12.fxml"));
    }

    @Override
    public void playExpert7(int cardID, Expert7 expert) {
        Expert7SceneController exp7 = new Expert7SceneController(cardID,expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp7,ChangeType.EXPERT,"Expert7.fxml"));
    }

    @Override
    public void playExpert10(int cardID, Expert10 expert) {

    }

    @Override
    public void playExpert3(int cardID, Expert3 expert) {
        Expert3SceneController exp3 = new Expert3SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp3,ChangeType.EXPERT,"Expert3.fxml"));
    }

    @Override
    public void playExpert1(int cardID, Expert1 expert) {
        Expert1SceneController exp1 = new Expert1SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp1,ChangeType.EXPERT,"Expert1.fxml"));
    }

    @Override
    public void playExpert5(int cardID, Expert5 expert) {
        Expert5SceneController exp5 = new Expert5SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp5,ChangeType.EXPERT,"Expert5.fxml"));
    }

    @Override
    public void chooseAction() {
        PlayerViewController pwc = getPWC();
        Platform.runLater(pwc::setPreparationPhaseChoiceBox);
    }

    @Override
    public void moveMotherNature() {
        PlayerViewController pwc = getPWC();
        Platform.runLater(pwc::enableMNMovement);    //used to move MN not in expert mode
    }

    public void chooseCloudTile(int cloudID) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(pwc::switchCloudStatus);
    }


    @Override
    public void showExpertCards(ArrayList<ExpertCard> allExpertCards,boolean expertPlayed, int numOfCoins) {
        ExpertCardSceneController exsx = new ExpertCardSceneController(allExpertCards,expertPlayed,numOfCoins);
        Platform.runLater(()->SceneController.showNewPopUp(list,exsx, ChangeType.EXPERT, "ExpertsCardScene.fxml","Experts"));
    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        //Utile solo per CLI
    }

    @Override
    public void chooseExpertCard() {

    }

    @Override
    public void moveMNplusExpert(boolean expertPlayed) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()->pwc.enableMNMovement());
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