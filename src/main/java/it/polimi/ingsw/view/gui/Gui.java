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

    /**
     * Method used to get from input the player nickname
     */
    @Override
    public void askPlayerNickname() {
        Platform.runLater(() -> SceneController.changeRoot(list, "NicknameInput.fxml"));
    }

    /**
     * Method called only on first player's interface to get the game parameters, like number of players and expert mode
     */
    @Override
    public void askGameParam() {
        Platform.runLater(() -> SceneController.changeRoot(list, "GameParamScene.fxml"));
    }

    /**
     * Method used to let player choose its deck and tower color based on a list of these parameters available
     *
     * @param remainingTowers list of remaining available towerColor
     * @param remainingDecks  list of remaining available deckType
     */
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

    /**
     * Method used to print the island list
     *
     * @param gameFieldMap map with the gameField
     */
    @Override
    public void showGameField(Map<Integer, IslandNode> gameFieldMap) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateIslands(gameFieldMap));
    }

    /**
     * Method used to print the students inside all the cloud tiles
     *
     * @param newClouds arrayList that contains all the clouds in the game
     */
    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateCloud(newClouds));
    }

    /**
     * Method used to print info about the active player and the gamePhase
     * @param currentPlayer nickName of the player that will play
     * @param currentState is the state of the game
     */
    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {

    }

    /**
     * Method used to print a generic message received from the server
     * @param genericMessage contains message
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        Platform.runLater(()->SceneController.showMessage(Alert.AlertType.INFORMATION,genericMessage));
    }

    /**
     * Method used to print the number of coins owned by the player
     * @param player    is the player that modified his number of coins
     * @param numOfCoin is the new value of coin of the player
     */
    @Override
    public void newCoin(String player, int numOfCoin) {

    }

    /**
     * Method used to get from the user the board he wants to check
     *
     * @param boardMap is a map of all the available boards in the game
     */
    @Override
    public void showBoard(Map<String, Board> boardMap) {

        BoardInfoSceneController boardInfoSceneController = new BoardInfoSceneController(boardMap);
        try {
            Platform.runLater(() -> SceneController.showNewPopUp(list, boardInfoSceneController, ChangeType.DEFAULT, "BoardInfoScene.fxml", "BoardInfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to print a player's board
     * @param board is the board that will be printed
     * @param player is the board's owner
     */
    @Override
    public void printBoard(Board board, String player) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(() -> pwc.populateBoard(board));
    }

    /**
     * Method used to print all the cards played in the previous turn
     * @param lastCard is a map where each card is associated to the player's nickname
     */
    @Override
    public void showLastUsedCard(Map<String, AssistantCard> lastCard) {
        AssistantCardsController assistantCardsController = new AssistantCardsController();
        assistantCardsController.setLastCard(lastCard);
        SceneController.popUpController = assistantCardsController;
    }

    /**
     * Method used to print the winning message
     *
     * @param winner nickname of the winner
     */
    @Override
    public void showWinner(String winner) {

    }

    /**
     * Method used to print an error message
     * @param errorMessage is the error message
     * @param errorType is the type of error
     */
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

    /**
     * Method used to print the available AssistantCards
     * @param deck is the player's deck
     */
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

    /**
     * Method used to get from input port and address required to connect to the server, notifies the controller with received values
     */
    @Override
    public void connectionRequest() {

    }

    /**
     * method to play expert card 9
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert9(int cardID, Expert9 expert) {
        Expert9_12_SceneController exp9 = new Expert9_12_SceneController(cardID);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp9,ChangeType.EXPERT,"Expert9.fxml"));
    }

    /**
     * method to play expert card 11
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert11(int cardID, Expert11 expert) {
        Expert11SceneController exp11 = new Expert11SceneController(cardID,expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp11,ChangeType.EXPERT,"Expert11.fxml"));
    }

    /**
     * method to play expert card 12
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert12(int cardID, Expert12 expert) {
        Expert9_12_SceneController exp9 = new Expert9_12_SceneController(cardID);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp9,ChangeType.EXPERT,"Expert12.fxml"));
    }

    /**
     * method to play expert card 7
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert7(int cardID, Expert7 expert) {
        Expert7SceneController exp7 = new Expert7SceneController(cardID,expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp7,ChangeType.EXPERT,"Expert7.fxml"));
    }

    /**
     * method to play expert card 10
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert10(int cardID, Expert10 expert) {
        Expert10SceneController exp10 = new Expert10SceneController(cardID,expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp10,ChangeType.EXPERT,"Expert10.fxml"));
    }

    /**
     * method to play expert card 3
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert3(int cardID, Expert3 expert) {
        Expert3SceneController exp3 = new Expert3SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp3,ChangeType.EXPERT,"Expert3.fxml"));
    }

    /**
     * method to play expert card 1
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert1(int cardID, Expert1 expert) {
        Expert1SceneController exp1 = new Expert1SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp1,ChangeType.EXPERT,"Expert1.fxml"));
    }

    /**
     * method to play expert card 5
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpert5(int cardID, Expert5 expert) {
        Expert5SceneController exp5 = new Expert5SceneController(cardID, expert);
        Platform.runLater(()->SceneController.showNewPopUp(list,exp5,ChangeType.EXPERT,"Expert5.fxml"));
    }

    /**
     * Method used to ask the player what to do during preparation phase
     */
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

    /**
     * Method used to choose the expert
     */
    @Override
    public void chooseExpertCard() {

    }

    /**
     * Method used to ask if the player wants to move mother nature and end its turn, or play an expert card
     */
    @Override
    public void moveMNplusExpert(boolean expertPlayed) {
        PlayerViewController pwc = getPWC();
        Platform.runLater(()->pwc.enableMNMovement());
    }

    @Override
    public void clear() {

    }

    /**
     * Method used to generate the default layout
     * @param gameFieldMap is a map containing the islands
     * @param chargedClouds is an arraylist containing all the infos about cloudTiles
     * @param boardMap is a map of all the boards
     * @param currentPlayer is the nickname of the actual player
     * @param experts is an arraylist of expert cards
     * @param numOfCoins is the number of coins owned by the player
     */
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