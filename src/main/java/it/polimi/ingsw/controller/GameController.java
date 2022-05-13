package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.FirstLoginMessage;
import it.polimi.ingsw.network.messages.LogInMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.PlayersNumberMessage;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.network.messages.MessageType.FIRST_LOGIN;
import static it.polimi.ingsw.network.messages.MessageType.LOGIN;

public class GameController {

    private Game game;
    private final Map<String,VirtualView> viewMap;
    private Player currentPlayer;
    private int numOfPlayers;
    private PreparationPhaseLogic preparationPhaseLogic;
    private GameState gameState;


    public GameController() {

        this.game = null;
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.currentPlayer = null;
        this.gameState = GameState.CONNECT;
    }

    public void onMessageReceived (Message receivedMessage) {

        VirtualView virtualView = viewMap.get(receivedMessage.getSenderPlayer());

        switch (gameState)  {
            case CONNECT:
                connectState(receivedMessage);
                nextState();
                break;
            case LOGIN:
                loginState(receivedMessage);
                nextState();
                break;

            case INIT:

                break;
        }
    }

    private void nextState() {

        GameState nextState = gameState;

        switch (gameState)  {
            case CONNECT:
                nextState = GameState.LOGIN;
                break;

            case LOGIN:
                if(game.getPlayersList().size() == game.NUM_OF_PLAYERS) {
                    preparationPhaseLogic.generatePlayingOrder();
                    setCurrentPlayer(preparationPhaseLogic.getActivePlayer());
                    nextState = GameState.INIT;
                }
                break;

            case INIT:

                break;
        }
        gameState = nextState;
    }

    private void connectState(Message receivedMessage) {
        setNumOfPlayers(((PlayersNumberMessage) receivedMessage).getNumOfPlayers());
    }

    private void loginState(Message receivedMessage){
        try {
            if (receivedMessage.getType() == FIRST_LOGIN) {
                game = GameManager.getInstance().initGame(((FirstLoginMessage) receivedMessage).getGameMode(), ((FirstLoginMessage) receivedMessage).isExpertMode());
                setPreparationPhaseLogic(new PreparationPhaseLogic(game));
            }
            else if (receivedMessage.getType() == LOGIN) {
                game.addPlayer(receivedMessage.getSenderPlayer(), ((LogInMessage) receivedMessage).getChosenDeckType(), ((LogInMessage) receivedMessage).getChosenTowerColor());
            }
            else
                throw new InvalidParameterException();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initState(Message receivedMessage){

    }

    public void logInHandler(String nickName, VirtualView virtualView) {

        if(viewMap.isEmpty()) {
            viewMap.put(nickName, virtualView);
            viewMap.get(nickName).askNumberOfPlayers();
        }
        else if (viewMap.size() <= numOfPlayers) {
            viewMap.put(nickName, virtualView);
        }
        else {
            virtualView.disconnect();
        }
    }

    /**Check if a nickname is already taken
     * @param nickname nickname to verify the validity of
     * @return {@code true} if the choice is valid or {@code false} if is already taken
     */
    public boolean checkNicknameValidity(String nickname){
        return !viewMap.containsKey(nickname);
    }

    /**Check if a nickname is already taken
     * @param colorChosen colorChosen to verify the validity of
     * @return {@code true} if the choice is valid or {@code false} if is already taken
     */
    public boolean checkTowerValidity(TowerColor colorChosen){
        return game.getAVAILABLE_TOWER_COLOR().contains(colorChosen);
    }

    public boolean checkAssistantValidity(DeckType assistantChosen){
        return game.getAVAILABLE_DECK_TYPE().contains(assistantChosen);
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PreparationPhaseLogic getPreparationPhaseLogic() {
        return preparationPhaseLogic;
    }

    public void setPreparationPhaseLogic(PreparationPhaseLogic preparationPhaseLogic) {
        this.preparationPhaseLogic = preparationPhaseLogic;
    }

    public Game getGame() {
        return game;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
