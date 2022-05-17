package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.client_messages.AssistantCardMessage;
import it.polimi.ingsw.network.messages.client_messages.CreatePlayerMessage;
import it.polimi.ingsw.network.messages.client_messages.MovedStudentsBoard;
import it.polimi.ingsw.network.messages.client_messages.MovedStudentsIslands;
import it.polimi.ingsw.network.messages.server_messages.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.network.messages.MessageType.*;
/**
 * Class GameController
 *
 * @author Miglia
 */
public class GameController {

    private Game game;
    private final Map<String,VirtualView> viewMap;
    private TurnLogic turnLogic;
    private GameState gameState;

    /**
     * Constructor, creates a GameController without any game.
     */
    public GameController() {

        this.game = null;
        this.viewMap = Collections.synchronizedMap(new HashMap<>());
        this.gameState = GameState.LOGIN;
    }

    /**
     * method to manage the received messages
     * @param receivedMessage received message
     */
    public void onMessageReceived (Message receivedMessage) {

        VirtualView virtualView = viewMap.get(receivedMessage.getSenderPlayer());

        switch (gameState)  {
            case LOGIN: //creates the game
                if(receivedMessage.getType() == GAMEPARAM)
                    gameCreation(receivedMessage);
                nextState();
                break;

            case CREATE_PLAYERS: //adds players to the game
                playersCreationState(receivedMessage);
                //sends updated list of remaining items to the other players
                Stream<Player> notLoggedPlayers =
                game.getPlayersList().stream().filter(name -> !viewMap.containsKey(name.getNickname()));
                List<Player> sendTo = notLoggedPlayers.collect(Collectors.toList());
                for (Player player : sendTo)
                    viewMap.get(player.getNickname()).showRemainingTowerAndDeck(game.getAVAILABLE_TOWER_COLOR(), game.getAVAILABLE_DECK_TYPE());

                nextState();
                break;

            case PREPARATION_PHASE: //PreparationPhase logic
                if(receivedMessage.getType() == CHARGECLOUD) {
                    for (String nickname : viewMap.keySet()){
                        viewMap.get(nickname).showChargedClouds(game.rechargeClouds());
                    }
                }
                else if (receivedMessage.getType() == PLAY_ASSISTANT_CARD) {

                    Player currentPlayer = game.getPlayerByNickName(receivedMessage.getSenderPlayer());

                    try {
                        turnLogic.setPlayedCard(((AssistantCardMessage)receivedMessage).getPlayedCard(), currentPlayer);
                    } catch (CardAlreadyPlayed e) {
                        throw new RuntimeException("CardAlreadyPlayed");
                    } catch (InexistentCard e) {
                        throw new RuntimeException("Card not found");
                    } catch (EndGameException e) {
                        throw new RuntimeException("Card not found");
                    }
                }
                nextState();
                break;

            case ACTION_PHASE: //ActionPhaseLogic

                if(receivedMessage.getType() == MOVE_TO_ISLAND) {
                    String nickName = receivedMessage.getSenderPlayer();
                    HashMap<Integer, ArrayList<Color>> studentsMoved = ((MovedStudentsIslands)receivedMessage).getMovedStudents();

                    for(int nodeID : studentsMoved.keySet()) {
                        for(Color currentColor : studentsMoved.get(nodeID)) {
                            try {
                                game.getPlayerByNickName(nickName).getBoard().moveToIsland(currentColor, nodeID);
                            } catch (NotOnBoardException e) {
                                throw new RuntimeException("Student not found");
                            }
                        }
                    }
                }

                if(receivedMessage.getType() == MOVE_TO_DINING) {
                    String nickName = receivedMessage.getSenderPlayer();
                    ArrayList<Color> studentsMoved = ((MovedStudentsBoard)receivedMessage).getMovedStudents();
                    List<Color> teachers = new ArrayList<>();

                    for(Color currentColor : studentsMoved) {

                        try {
                            game.getPlayerByNickName(nickName).getBoard().moveEntryToDiningRoom(currentColor);
                        } catch (NotOnBoardException | NotEnoughSpace e) {
                            throw new RuntimeException("Student not found or not enoughSpace");
                        }

                        if(game.checkInfluence(game.getPlayerByNickName(nickName), currentColor))
                            teachers.add(currentColor);
                    }


                }

                if(receivedMessage.getType() == PLAY_EXPERT_CARD) {
                    //Questo greve
                }

                nextState();
                break;
        }

    }

    /**
     * method to manage the switch between game states
     */
    private void nextState() {

        GameState nextState = gameState;

        switch (gameState)  {
            case LOGIN: //verifies that all the VirtualViews are set

                if(viewMap.size() == game.NUM_OF_PLAYERS) {
                    broadcast("All players logged");
                    nextState = GameState.CREATE_PLAYERS;
                }
                break;

            case CREATE_PLAYERS: //verifies that all the Players are created
                if(game.getPlayersList().size() == game.NUM_OF_PLAYERS) {
                    //sets first player
                    turnLogic.generatePreparationPhaseOrder();
                    //generates a GameFieldMap
                    Map<Integer, Node> gameFieldMap = new HashMap<>();
                    for (int i = 1; i <= game.getGameField().size(); i++) {
                        gameFieldMap.put(i, game.getGameField().getIslandNode(i));
                    }

                    //sends to each player the current situation on the board, giving also a coin if expert mode is on
                    for(Player currentPlayer : game.getPlayersList())
                    {
                        VirtualView currentView = viewMap.get(currentPlayer.getNickname());
                        ArrayList<Color> currentEntranceHall = currentPlayer.getBoard().getEntryRoom();

                        if(game.EXP_MODE)
                            game.coinHandler(currentPlayer, 1);

                        //message for init the player
                        currentView.showInitPlayer(game.MAX_NUM_OF_TOWERS, currentEntranceHall);

                        //sends the gameField to each Player
                        currentView.showGameField(gameFieldMap);

                        //sends to the first player a current player message
                        if(currentPlayer.getNickname().equals(turnLogic.getActivePlayer().getNickname()))
                            currentView.showCurrentPlayer(currentPlayer.getNickname());
                    }

                    //broadcast the start of the preparationPhase
                    broadcast("PreparationPhase started");

                    nextState = GameState.PREPARATION_PHASE;
                }
                break;

            case PREPARATION_PHASE:

                if(turnLogic.getCardsPlayed().size() == game.NUM_OF_PLAYERS) {
                    broadcast("Start ActionPhase");
                    viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname());
                    nextState = GameState.ACTION_PHASE;
                }
                else
                    viewMap.get(turnLogic.nextActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname());
                break;

            case ACTION_PHASE:
        }
        gameState = nextState;
    }

    /**
     * method that creates the game
     * @param receivedMessage, must be a GameParam message, contains the game parameters
     */
    private void gameCreation(Message receivedMessage) {

        int numOfPlayers = ((GameParamMessage) receivedMessage).getNumOfPlayers();
        boolean expertMode = ((GameParamMessage) receivedMessage).isExpertMode();
        game = GameManager.getInstance().initGame(fromIntToGameMode(numOfPlayers), expertMode);
        setTurnLogic(new TurnLogic(game));

    }

    /**
     * method that creates players
     * @param receivedMessage, must be a CreatePlayerMessage, contains the player parameters
     */
    private void playersCreationState(Message receivedMessage){
        try {
            if (receivedMessage.getType() == PLAYER_CREATION) {
                String nick = receivedMessage.getSenderPlayer();
                DeckType DT = ((CreatePlayerMessage) receivedMessage).getChosenDeckType();
                TowerColor TC = ((CreatePlayerMessage) receivedMessage).getChosenTowerColor();
                game.addPlayer(nick,DT,TC);
            }
            else
                throw new InvalidParameterException();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that associates a virtual view and a nickname
     * @param nickName nickname of the player
     * @param virtualView virtual view of the player
     */
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

    /**
     * method to convert int in gameMode
     * @param numOfPlayers number of players
     */
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

    /**
     * method to send a broadCast Message
     * @param genericMessage message to broadCast
     */
    private void broadcast(String genericMessage) {

        for (String nickname : viewMap.keySet()){
            viewMap.get(nickname).showGenericMessage(genericMessage);
        }

    }
    public GameState getGameState() {
        return gameState;
    }

    public TurnLogic getTurnLogic() {
        return turnLogic;
    }

    public void setTurnLogic(TurnLogic turnLogic) {
        this.turnLogic = turnLogic;
    }

    public Game getGame() {
        return game;
    }

}
