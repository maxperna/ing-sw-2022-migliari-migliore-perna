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

import static it.polimi.ingsw.network.messages.MessageType.EXPERTMODE;
import static it.polimi.ingsw.network.messages.MessageType.LOGIN;

public class GameController {

    private Game game;
    private final Map<String,VirtualView> viewMap;
    private Player currentPlayer;
    private int numOfPlayers;
    private TurnLogic turnLogic;
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
                virtualView.remainingTowerAndDeck(game.getAVAILABLE_TOWER_COLOR(), game.getAVAILABLE_DECK_TYPE());
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
                    turnLogic.generatePlayingOrder();
                    setCurrentPlayer(turnLogic.getActivePlayer());
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
            if (receivedMessage.getType() == EXPERTMODE) {
                game = GameManager.getInstance().initGame(fromIntToGameMode(numOfPlayers), ((FirstLoginMessage) receivedMessage).isExpertMode());
                setPreparationPhaseLogic(new TurnLogic(game));
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

    private String fromIntToGameMode(int numOfPlayers) {
        switch (numOfPlayers) {
            case 2:
                return "TwoPlayers";

            case 3:
                return "ThreePlayers";

            case 4:
                return "FourPlayers";

            default:
                return "Unknown";
        }
    }


    /**Check if a nickname is already taken
     * @param nickname nickname to verify the validity of
     * @return {@code true} if the choice is valid or {@code false} if is already taken
     */
    public boolean checkNicknameValidity(String nickname){
        return !viewMap.containsKey(nickname);
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

    public TurnLogic getPreparationPhaseLogic() {
        return turnLogic;
    }

    public void setPreparationPhaseLogic(TurnLogic turnLogic) {
        this.turnLogic = turnLogic;
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
