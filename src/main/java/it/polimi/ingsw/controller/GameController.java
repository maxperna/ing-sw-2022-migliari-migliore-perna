package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.view.VirtualView;
import org.jetbrains.annotations.TestOnly;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.messages.MessageType.*;
/**
 * Class GameController
 *
 * @author Miglia
 */
public class GameController implements PropertyChangeListener {

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
    public synchronized void onMessageReceived (Message receivedMessage) {

        String senderPlayer = receivedMessage.getSenderPlayer();

        switch (gameState)  {
            case LOGIN: //creates the game
                if(viewMap.containsKey(senderPlayer) && receivedMessage.getType() == GAMEPARAM)
                    gameCreation(receivedMessage);

                nextState();
                break;

            case CREATE_PLAYERS: //adds players to the game

                playersCreationState(receivedMessage);
                //sends updated list of remaining items to the other players
                List<String> createdPlayers = new ArrayList<>();
                for (Player currentPlayer : game.getPlayersList())
                    createdPlayers.add(currentPlayer.getNickname());

                List<String> diff = viewMap.keySet().stream()
                    .filter(name -> !createdPlayers.contains(name))
                    .collect(Collectors.toList());

                if(!diff.isEmpty())
                    viewMap.get(diff.get(0)).showRemainingTowerAndDeck(game.getAVAILABLE_TOWER_COLOR(), game.getAVAILABLE_DECK_TYPE());

                nextState();
                break;

            case PREPARATION_PHASE: //PreparationPhase logic

                if(receivedMessage.getType() == ASSISTANT_INFO)
                {
                    viewMap.get(senderPlayer).showAssistant(game.getPlayerByNickName(senderPlayer).getDeck().getRemainingCards());
                }

                if (receivedMessage.getType() == PLAY_ASSISTANT_CARD) {

                    Player currentPlayer = game.getPlayerByNickName(senderPlayer);

                    try {
                        //plays the card
                        turnLogic.setPlayedCard(((PlayAssistantMessage)receivedMessage).getPlayedCard(), currentPlayer);
                        nextState();
                    }
                    catch (CardAlreadyPlayed e) {
                        for (String nickName : viewMap.keySet()) {
                            viewMap.get(nickName).showError("CardAlreadyPlayed");
                        }
                    }
                    catch (InexistentCard e) {
                        for (String nickName : viewMap.keySet()) {
                            viewMap.get(nickName).showError("Card not found");
                        }
                    }
                    catch (EndGameException e) {

                        Player winner = game.getPlayersList().get(0);
                        Player equal = game.getPlayersList().get(0);

                        for(Player player : game.getPlayersList()) {
                            if(winner.getBoard().getNumOfTowers() > player.getBoard().getNumOfTowers())
                                winner = player;
                            if(winner.getBoard().getNumOfTowers().equals(player.getBoard().getNumOfTowers()))
                                equal = player;
                        }

                        if(!equal.equals(winner) && game.NUM_OF_PLAYERS != 4) {
                            for (String nickName : viewMap.keySet()) {
                                viewMap.get(nickName).showWinner("Patta");
                            }
                        }
                        else if (game.NUM_OF_PLAYERS == 4) {
                            if(winner.getBoard().getTeamMate().equals(equal)){
                                for (String nickName : viewMap.keySet()) {
                                    viewMap.get(nickName).showWinner(winner.getNickname() + winner.getBoard().getTeamMate().getNickname());
                                }
                            }
                            else
                                for (String nickName : viewMap.keySet()) {
                                    viewMap.get(nickName).showWinner("Patta");
                                }
                        }
                        else
                            for (String nickName : viewMap.keySet()) {
                                viewMap.get(nickName).showWinner(winner.getNickname());
                            }
                    }
                }

                showGameInfo(receivedMessage, senderPlayer);

                break;

            case ACTION_PHASE: //ActionPhaseLogic

                if(receivedMessage.getType() == MOVE_TO_ISLAND) {

                    Color currentColor = ((MovedStudentToIsland)receivedMessage).getMovedStudent();
                    int islandID = ((MovedStudentToIsland)receivedMessage).getTargetIsland();

                    try {
                        game.getPlayerByNickName(senderPlayer).getBoard().moveToIsland(currentColor, islandID);
                    } catch (NotOnBoardException e) {

                        for (String nickName : viewMap.keySet()) {
                            viewMap.get(nickName).showError("Student not found");
                        }
                    }

                }

                if(receivedMessage.getType() == MOVE_TO_DINING) {
                    Color studentMoved = ((MovedStudentToBoard)receivedMessage).getMovedStudent();

                        try {
                            game.getPlayerByNickName(senderPlayer).getBoard().moveEntryToDiningRoom(studentMoved);
                            game.checkInfluence(game.getPlayerByNickName(senderPlayer), studentMoved);
                        } catch (NotOnBoardException e) {
                            for (String nickName : viewMap.keySet()) {
                                viewMap.get(nickName).showError("Student not found");
                            }
                        } catch (NotEnoughSpace e) {
                            for (String nickName : viewMap.keySet()) {
                                viewMap.get(nickName).showError("Not enoughSpace");
                            }
                        }

                }

                if(receivedMessage.getType() == MOVE_MOTHER_NATURE){
                    int motherNatureSteps = ((MoveMotherNatureMessage)receivedMessage).getNumOfSteps();
                    try {
                        turnLogic.moveMotherNature(game.getPlayerByNickName(senderPlayer),motherNatureSteps);
                    }
                    catch (EndGameException e) {
                        for (String nickName : viewMap.keySet()) {
                            viewMap.get(nickName).showWinner(senderPlayer);
                        }
                    }catch (IllegalMove IL){
                        viewMap.get(senderPlayer).showError("Too much steps");
                    }
                    finally {
                        nextState();
                    }
                }

                if(receivedMessage.getType() == PLAY_EXPERT_CARD) {
                    expertsHandling((PlayExpertCard) receivedMessage);
                }

                showGameInfo(receivedMessage, senderPlayer);

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
                if(game != null && viewMap.size() == game.NUM_OF_PLAYERS) {
                    broadcast("All players logged");
                    Object firstKey = viewMap.keySet().toArray()[getGame().NUM_OF_PLAYERS-1];
                    viewMap.get((String)firstKey).showRemainingTowerAndDeck(game.getAVAILABLE_TOWER_COLOR(),game.getAVAILABLE_DECK_TYPE());
                    nextState = GameState.CREATE_PLAYERS;
                }
                break;

            case CREATE_PLAYERS: //verifies that all the Players are created

                if(game.getPlayersList().size() == game.NUM_OF_PLAYERS) {
                    //sets first player
                    turnLogic.generatePreparationPhaseOrder();
                    //generates a GameFieldMap
                    Map<Integer, Node> gameFieldMap = generateGameFieldMap();
                    //generate ExpertList
                    ArrayList<ExpertID> expertIDList = new ArrayList<>();
                    for(ExpertCard currentCard : game.getExpertsCard())
                        expertIDList.add(currentCard.getExpType());

                    //sends to each player the current situation on the board, giving also a coin if expert mode is on and the GameFieldMap
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

                        //sends the expertList
                        currentView.showExpertCards(expertIDList);

                        //sends to the first player a current player message
                        if(currentPlayer.getNickname().equals(turnLogic.getActivePlayer().getNickname()))
                            currentView.showCurrentPlayer(currentPlayer.getNickname(), GameState.PREPARATION_PHASE);
                    }

                    setListeners();

                    //broadcast the start of the preparationPhase
                    broadcast("Start PreparationPhase");

                    game.rechargeClouds();

                    nextState = GameState.PREPARATION_PHASE;

                }
                break;

            case PREPARATION_PHASE:

                if(turnLogic.getCardsPlayed().size() == game.NUM_OF_PLAYERS) {
                    //Switches turnLogic in actionPhase
                    turnLogic.switchPhase();
                    viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), GameState.ACTION_PHASE);
                    broadcast("Start ActionPhase");
                    nextState = GameState.ACTION_PHASE;
                }
                else {
                    //Sends to the next player a message
                    Player nextPlayerAction = turnLogic.nextActivePlayer();
                    if(nextPlayerAction != null) {
                        viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), gameState);
                    }
                }
                break;

            case ACTION_PHASE:

                if(turnLogic.nextActivePlayer() == null)
                {
                    turnLogic.switchPhase();
                    viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), GameState.PREPARATION_PHASE);
                    broadcast("Start PreparationPhase");
                    nextState = GameState.PREPARATION_PHASE;
                }
                else
                    viewMap.get(turnLogic.nextActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), gameState);

                break;
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

        String nick = receivedMessage.getSenderPlayer();

        try {
            if (receivedMessage.getType() == PLAYER_CREATION) {
                DeckType DT = ((CreatePlayerMessage) receivedMessage).getChosenDeckType();
                TowerColor TC = ((CreatePlayerMessage) receivedMessage).getChosenTowerColor();
                game.addPlayer(nick,DT,TC);
            }

        } catch (FileNotFoundException e) {
            viewMap.get(nick).showError("File Not Found");
        }
    }

    /**Method to apply the right effect to the experts cards
     * @param message play expert card message*/
    public void expertsHandling(PlayExpertCard message){
        Player player = game.getPlayerByNickName(message.getSenderPlayer());
        int playedCard = message.getPlayedCard();
        try {
            switch (message.getExpID()) {
                case 1:
                    turnLogic.playExpertCard(player, playedCard);
                case 2:
                    PlayExpertCard2 castedMessage1 = (PlayExpertCard2) message;
                    turnLogic.playExpertCard(player, castedMessage1.getNodeID(), playedCard);
                case 3:
                    assert message instanceof PlayExpertCard3;
                    PlayExpertCard3 castedMessage2 = (PlayExpertCard3) message;
                    turnLogic.playExpertCard(player, castedMessage2.getNodeID(), castedMessage2.getStudent(), playedCard);
                case 4:
                    assert message instanceof PlayExpertCard4;
                    PlayExpertCard4 castedMessage3 = (PlayExpertCard4) message;
                    turnLogic.playExpertCard(player, castedMessage3.getStudent(), playedCard);
                case 5:
                    assert message instanceof PlayExpertCard5;
                    PlayExpertCard5 castedMessage4 = (PlayExpertCard5) message;
                    turnLogic.playExpertCard(player, castedMessage4.getStudents1(), castedMessage4.getStudents2(), playedCard);
            }
        }catch (IllegalMove|NotEnoughCoins e){
            viewMap.get(message.getSenderPlayer()).showError("Cannot play this card");
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
            onMessageReceived(new GenericMessage("SYNC"));
        }
        else {
            virtualView.disconnect();
        }


    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if(event.getPropertyName().equals("UpdateCloud")) {
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).showClouds(game.getCloudTiles());
            }
        }

        if(event.getPropertyName().contains("UpdateNode")) {

            String value = event.getPropertyName();
            String intValue = value.replaceAll("\\D+","");
            int nodeID = Integer.parseInt(intValue);

            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).updateNode(game.getGameField().getIslandNode(nodeID));
            }
        }

        if(event.getPropertyName().equals("UpdateTeacher")) {
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).updateTeachers(game.getPlayerByNickName(nickName).getBoard().getTeachers());
            }
        }

        if(event.getPropertyName().equals("Merge")) {
            Map<Integer, Node> gameFieldMap = generateGameFieldMap();

            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).showGameField(gameFieldMap);
            }
        }

        if(event.getPropertyName().contains("UpdateCoin")) {

            String value = event.getPropertyName();
            String playerName = value.substring(10);
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).newCoin(playerName, game.getPlayerByNickName(playerName).getNumOfCoin());
            }
        }
    }

    public void showGameInfo(Message messageReceived, String senderPlayer) {

        if(messageReceived.getType() == CHARGECLOUD)
            viewMap.get(senderPlayer).showClouds(game.getCloudTiles());

        if(messageReceived.getType() == SHOW_BOARD) {
            Map<String, Board> boardMap = new HashMap<>();

            for(Player currentPlayer : game.getPlayersList()) {
                if(!currentPlayer.getNickname().equals(senderPlayer))
                    boardMap.put(currentPlayer.getNickname(), currentPlayer.getBoard());
            }

            viewMap.get(senderPlayer).showBoard(boardMap);
        }

        if(messageReceived.getType() == ASSISTANT_INFO)
            viewMap.get(senderPlayer).showAssistant(game.getPlayerByNickName(senderPlayer).getDeck().getRemainingCards());

        if(messageReceived.getType() == LAST_ASSISTANT) {
            Map<String, AssistantCard> lastCardMap = new HashMap<>();

            for(Player currentPlayer : game.getPlayersList()) {
                if(!currentPlayer.getNickname().equals(senderPlayer))
                    lastCardMap.put(currentPlayer.getNickname(), currentPlayer.getDeck().getLastCard());
            }

            viewMap.get(senderPlayer).showPlayedAssistantCard(lastCardMap);
        }

        if(messageReceived.getType() == GAME_FIELD)
            viewMap.get(senderPlayer).showGameField(generateGameFieldMap());

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

    private Map<Integer, Node> generateGameFieldMap() {
        Map<Integer, Node> gameFieldMap = new HashMap<>();
        for (int i = 1; i <= game.getGameField().size(); i++) {
            gameFieldMap.put(i, game.getGameField().getIslandNode(i));
        }
        return gameFieldMap;
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

    public void setListeners() {

        for(CloudTile currentCloud : game.getCloudTiles()) {
            currentCloud.addPropertyChangeListener(this);
        }

        for(int i = 1; i < game.getGameField().size(); i ++) {
            game.getGameField().getIslandNode(i).addPropertyChangeListener(this);
        }

        for(Player currentPlayer : game.getPlayersList()) {
            currentPlayer.getBoard().addPropertyChangeListener(this);
        }

        game.addPropertyChangeListener(this);

        game.getGameField().addPropertyChangeListener(this);
    }

    public void removeListeners() {

        for(CloudTile currentCloud : game.getCloudTiles()) {
            currentCloud.removePropertyChangeListener(this);
        }

        for(int i = 1; i < game.getGameField().size(); i ++) {
            game.getGameField().getIslandNode(i).removePropertyChangeListener(this);
        }

        for(Player currentPlayer : game.getPlayersList()) {
            currentPlayer.getBoard().removePropertyChangeListener(this);
        }

        game.removePropertyChangeListener(this);

        game.getGameField().removePropertyChangeListener(this);
    }

    public Map<String, VirtualView> getViewMap() {
        return viewMap;
    }

    @TestOnly
    public void loginForTest(String nickName) {

        logInHandler(nickName, new VirtualView());
    }
}
