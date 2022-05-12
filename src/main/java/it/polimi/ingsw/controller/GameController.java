package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.FirstLoginMessage;
import it.polimi.ingsw.network.messages.LogInMessage;
import it.polimi.ingsw.network.messages.Message;
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
    private PreparationPhaseLogic preparationPhaseLogic;
    private GameState gameState;


    public GameController(String gameMode, boolean expertMode) {

        this.game = null;
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.currentPlayer = new Player();
        this.viewMap.put("Default",new VirtualView());
        this.gameState = GameState.LOGIN;
    }

    public void onMessageReceived (Message receivedMessage) {

        VirtualView virtualView = viewMap.get(currentPlayer.getNickname());

        switch (gameState)  {
            case LOGIN:
                loginState(receivedMessage);
                nextState();
                break;

            case INIT:
                viewMap.remove("Default");
                break;
        }
    }

    private void nextState() {

        GameState nextState = gameState;

        switch (gameState)  {
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

    private void loginState(Message receivedMessage){
        try {
            if (receivedMessage.getType() == FIRST_LOGIN) {
                game = GameManager.getInstance().initGame(((FirstLoginMessage) receivedMessage).getGameMode(), ((FirstLoginMessage) receivedMessage).isExpertMode());
                game.addPlayer(receivedMessage.getSenderPlayer(), ((FirstLoginMessage) receivedMessage).getChosenDeckType(), ((FirstLoginMessage) receivedMessage).getChosenTowerColor());
                viewMap.put(receivedMessage.getSenderPlayer(), new VirtualView());
                setPreparationPhaseLogic(new PreparationPhaseLogic(game));
            }
            else if (receivedMessage.getType() == LOGIN) {
                game.addPlayer(receivedMessage.getSenderPlayer(), ((LogInMessage) receivedMessage).getChosenDeckType(), ((LogInMessage) receivedMessage).getChosenTowerColor());
                viewMap.put(receivedMessage.getSenderPlayer(), new VirtualView());
            }
            else
                throw new InvalidParameterException();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initState(Message receivedMessage){
        viewMap.remove("Default");
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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
}
