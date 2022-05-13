package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.CreatePlayerMessage;
import it.polimi.ingsw.network.messages.EndLogInMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.GameParamMessage;
import it.polimi.ingsw.network.messages.replies.RemainingItemReply;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.network.messages.MessageType.*;

public class GameController {

    private Game game;
    private final Map<String,VirtualView> viewMap;
    private Player currentPlayer;
    private PreparationPhaseLogic preparationPhaseLogic;
    private GameState gameState;


    public GameController() {

        this.game = null;
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.currentPlayer = null;
        this.gameState = GameState.LOGIN;
    }

    public void onMessageReceived (Message receivedMessage) {

        VirtualView virtualView = viewMap.get(receivedMessage.getSenderPlayer());

        switch (gameState)  {
            case LOGIN:
                if(receivedMessage.getType()== GAMEPARAM)
                    gameCreation(receivedMessage);
                nextState();
                break;

            case CREATE_PLAYERS:
                playersCreationState(receivedMessage);
                //sends updated list of remaining items to the other players
                Stream<Player> notLoggedPlayers =
                game.getPlayersList().stream().filter(name -> !viewMap.containsKey(name.getNickname()));
                List<Player> sendTo = notLoggedPlayers.collect(Collectors.toList());
                for (Player player : sendTo)
                    viewMap.get(player.getNickname()).getClientHandler().sendMessage(new RemainingItemReply(game.getAVAILABLE_TOWER_COLOR(), game.getAVAILABLE_DECK_TYPE()));

                nextState();
                break;

            case INIT:
                preparationPhaseLogic.generatePlayingOrder();
                setCurrentPlayer(preparationPhaseLogic.getActivePlayer());

                break;
        }
    }

    private void nextState() {

        GameState nextState = gameState;

        switch (gameState)  {
            case LOGIN:

                if(viewMap.size() == game.NUM_OF_PLAYERS) {
                    broadcast(new EndLogInMessage());
                    nextState = GameState.CREATE_PLAYERS;
                }
                break;

            case CREATE_PLAYERS:
                if(game.getPlayersList().size() == game.NUM_OF_PLAYERS) {
                    nextState = GameState.INIT;
                }
                break;

            case INIT:

                break;
        }
        gameState = nextState;
    }

    private void gameCreation(Message receivedMessage) {

        int numOfPlayers = ((GameParamMessage) receivedMessage).getNumOfPlayers();
        boolean expertMode = ((GameParamMessage) receivedMessage).isExpertMode();
        game = GameManager.getInstance().initGame(fromIntToGameMode(numOfPlayers), expertMode);
        setPreparationPhaseLogic(new PreparationPhaseLogic(game));

    }

    private void playersCreationState(Message receivedMessage){
        try {
            if (receivedMessage.getType() == PLAYER_CREATION) {
                game.addPlayer(receivedMessage.getSenderPlayer(), ((CreatePlayerMessage) receivedMessage).getChosenDeckType(), ((CreatePlayerMessage) receivedMessage).getChosenTowerColor());
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
            viewMap.get(nickName).askGameParam();
        }
        else if (viewMap.size() <= game.NUM_OF_PLAYERS) {
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

    private void broadcast(Message messageToSend) {

        for (String nickname : viewMap.keySet()){
            viewMap.get(nickname).getClientHandler().sendMessage(messageToSend);
        }

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

}
