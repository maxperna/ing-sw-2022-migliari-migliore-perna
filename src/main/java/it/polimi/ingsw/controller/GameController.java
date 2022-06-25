package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.view.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.messages.ErrorType.*;
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


                if (receivedMessage.getType() == PLAY_ASSISTANT_CARD) {

                    Player currentPlayer = game.getPlayerByNickName(senderPlayer);

                    try {
                        //plays the card
                        turnLogic.setPlayedCard(((PlayAssistantMessage)receivedMessage).getPlayedCard(), currentPlayer);
                        //Manda ultima carta giocata a tutti i giocatori
                        nextState();
                    }
                    catch (CardAlreadyPlayed e) {
                            viewMap.get(senderPlayer).showError("CardAlreadyPlayed", ASSISTANT_ERROR);
                    }
                    catch (InexistentCard e) {
                            viewMap.get(senderPlayer).showError("Card not found", ASSISTANT_ERROR);
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
                        viewMap.get(senderPlayer).showError("Student not found", STUDENT_ERROR);
                    } catch (IllegalMove e) {
                        viewMap.get(senderPlayer).showError("Island does not exists", STUDENT_ERROR);
                    }

                }

                if(receivedMessage.getType() == MOVE_TO_DINING) {
                    Color studentMoved = ((MovedStudentToBoard)receivedMessage).getMovedStudent();

                        try {
                            game.getPlayerByNickName(senderPlayer).getBoard().moveEntryToDiningRoom(studentMoved);
                            game.checkInfluence(game.getPlayerByNickName(senderPlayer), studentMoved);
                        } catch (NotOnBoardException e) {
                            viewMap.get(senderPlayer).showError("Student not found", STUDENT_ERROR);
                        } catch (NotEnoughSpace e) {
                            viewMap.get(senderPlayer).showError("Not enoughSpace", STUDENT_ERROR);
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
                        viewMap.get(senderPlayer).showError("Too many steps", MOTHER_NATURE_ERROR);
                    }
                }

                if(receivedMessage.getType() == GET_CLOUD) {
                    try {
                        int cloudID = ((GetCloudsMessage)receivedMessage).getCloudID();
                        ArrayList<Color> extractedStudents = game.getCloudTiles().get(cloudID).moveStudents();
                        game.getPlayerByNickName(senderPlayer).getBoard().addStudentsEntryRoom(extractedStudents);

                        nextState();
                    } catch (EmptyCloudException e) {
                        viewMap.get(senderPlayer).showError("EmptyCloud", CLOUD_ERROR);
                    } catch (NotEnoughSpace e) {
                        viewMap.get(senderPlayer).showError("NotEnoughSpace", CLOUD_ERROR);
                    }

                }


                if(receivedMessage.getType() == PLAY_EXPERT_CARD) {
                    expertsHandling((PlayExpertCard) receivedMessage);
                }

                if(receivedMessage.getType() == STUDENT_REQUEST) {
                    StudentsAvailableRequest studentsAvailableRequest = (StudentsAvailableRequest)receivedMessage;
                    ArrayList<Color> availableStudents = new ArrayList<>(game.getPlayerByNickName(senderPlayer).getBoard().getEntryRoom());
                    viewMap.get(senderPlayer).availableStudents(availableStudents, studentsAvailableRequest.getTypeOfMovement(), game.getGameField().size());
                }


                showGameInfo(receivedMessage, senderPlayer);
            break;

        }

    }



    /**
     * method to manage the switch between game states
     */
    private synchronized void nextState() {

        GameState nextState = gameState;

        switch (gameState)  {
            case LOGIN: //verifies that all the VirtualViews are set
                if(game != null && viewMap.size() == game.NUM_OF_PLAYERS) {
                    broadcast("All players logged");
                    Object firstKey = viewMap.keySet().toArray()[getGame().NUM_OF_PLAYERS-1];
                    viewMap.get((String)firstKey).showRemainingTowerAndDeck(game.getAVAILABLE_TOWER_COLOR(),game.getAVAILABLE_DECK_TYPE());

                    for(String nickName : viewMap.keySet())
                        viewMap.get(nickName).sendNumberOfPlayers(game.NUM_OF_PLAYERS);

                    nextState = GameState.CREATE_PLAYERS;
                }
                break;

            case CREATE_PLAYERS: //verifies that all the Players are created

                if(game.getPlayersList().size() == game.NUM_OF_PLAYERS) {
                    //sets first player
                    turnLogic.generatePreparationPhaseOrder();
                    //generates a GameFieldMap

                    //sends to each player the current situation on the board, giving also a coin if expert mode is on and the GameFieldMap
                    for(Player currentPlayer : game.getPlayersList())
                    {
                        VirtualView currentView = viewMap.get(currentPlayer.getNickname());
                        ArrayList<Color> currentEntranceHall = currentPlayer.getBoard().getEntryRoom();

                        if(game.EXP_MODE)
                            game.coinHandler(currentPlayer, 1);

                        //message for init the player
                        currentView.showInitPlayer(game.MAX_NUM_OF_TOWERS, currentEntranceHall);


                    }

                    setListeners();

                    //broadcast the start of the preparationPhase
                    for(String nickname: viewMap.keySet())
                        viewMap.get(nickname).startGame();


                    game.rechargeClouds();

                    //sends to the first player a current player message
                    String currentPlayer = turnLogic.getActivePlayer().getNickname();
                    viewMap.get(currentPlayer).showCurrentPlayer(currentPlayer, GameState.PREPARATION_PHASE);

                    nextState = GameState.PREPARATION_PHASE;

                    for (String nickName : viewMap.keySet()) {
                        viewMap.get(nickName).setExpertMode(game.EXP_MODE);
                    }

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
                    Player nextPlayer = turnLogic.nextActivePlayer();
                    if(nextPlayer != null) {
                        viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), gameState);
                    }
                    else
                        System.out.println("Qualcosa non va, non dovrebbe essere null questo valore");
                }
                break;

            case ACTION_PHASE:
                viewMap.get(turnLogic.getActivePlayer().getNickname()).newCoin(turnLogic.getActivePlayer().getNickname(), game.getPlayerByNickName(turnLogic.getActivePlayer().getNickname()).getNumOfCoin());
                Player nextPlayer = turnLogic.nextActivePlayer();
                if(nextPlayer == null)
                {
                    turnLogic.switchPhase();
                    viewMap.get(turnLogic.getActivePlayer().getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), GameState.PREPARATION_PHASE);
                    broadcast("Start PreparationPhase");
                    nextState = GameState.PREPARATION_PHASE;
                    game.rechargeClouds();
                }
                else
                    viewMap.get(nextPlayer.getNickname()).showCurrentPlayer(turnLogic.getActivePlayer().getNickname(), gameState);
                if(game.EXP_MODE)
                    for(String nickname : viewMap.keySet())
                        viewMap.get(nickname).expertModeControl(true);
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
            viewMap.get(nick).showError("File Not Found", PLAYER_CREATION_ERROR);
        }
    }

    /**Method to apply the right effect to the experts cards
     * @param message play expert card message*/
    public void expertsHandling(PlayExpertCard message){
        Player player = game.getPlayerByNickName(message.getSenderPlayer());
        int playedCard = message.getPlayedCard();
        try {
           /* if(message instanceof PlayExpertCard1) {
                turnLogic.playExpertCard(player, playedCard);
                viewMap.get(message.getSenderPlayer()).ActionPhaseTurn(false);
            }
            else if(message instanceof PlayExpertCard2) {
                PlayExpertCard2 castedMessage1 = (PlayExpertCard2) message;
                turnLogic.playExpertCard(player, castedMessage1.getNodeID(), playedCard);
                viewMap.get(message.getSenderPlayer()).ActionPhaseTurn(false);
            }
            else if(message instanceof PlayExpertCard3) {
                PlayExpertCard3 castedMessage2 = (PlayExpertCard3) message;
                turnLogic.playExpertCard(player, castedMessage2.getNodeID(), castedMessage2.getStudent(), playedCard);
                viewMap.get(message.getSenderPlayer()).ActionPhaseTurn(false);
            }
            else if(message instanceof PlayExpertCard4) {
                PlayExpertCard4 castedMessage3 = (PlayExpertCard4) message;
                turnLogic.playExpertCard(player, castedMessage3.getStudent(), playedCard);
                viewMap.get(message.getSenderPlayer()).ActionPhaseTurn(false);
            }
            else if(message instanceof PlayExpertCard5) {
                PlayExpertCard5 castedMessage4 = (PlayExpertCard5) message;
                turnLogic.playExpertCard(player, castedMessage4.getStudents1(), castedMessage4.getStudents2(), playedCard);
                viewMap.get(message.getSenderPlayer()).ActionPhaseTurn(false);
            }*/
            switch (message.getExpID()) {
                case 1:
                    turnLogic.playExpertCard(player, playedCard);
                    break;
                case 2:
                    assert message instanceof PlayExpertCard2;
                    PlayExpertCard2 castedMessage1 = (PlayExpertCard2) message;
                    turnLogic.playExpertCard(player, castedMessage1.getNodeID(), playedCard);
                    break;
                case 3:
                    assert message instanceof PlayExpertCard3;
                    PlayExpertCard3 castedMessage2 = (PlayExpertCard3) message;
                    turnLogic.playExpertCard(player, castedMessage2.getNodeID(), castedMessage2.getStudent(), playedCard);
                    break;
                case 4:
                    assert message instanceof PlayExpertCard4;
                    PlayExpertCard4 castedMessage3 = (PlayExpertCard4) message;
                    turnLogic.playExpertCard(player, castedMessage3.getStudent(), playedCard);
                    break;
                case 5:
                    assert message instanceof PlayExpertCard5;
                    PlayExpertCard5 castedMessage4 = (PlayExpertCard5) message;
                    turnLogic.playExpertCard(player, castedMessage4.getStudents1(), castedMessage4.getStudents2(), playedCard);
                    break;
            }

            for(String nickname : viewMap.keySet())
                viewMap.get(nickname).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), message.getSenderPlayer(), game.getExpertsCard(), game.getPlayerByNickName(nickname).getNumOfCoin());
            viewMap.get(message.getSenderPlayer()).expertModeControl(false);

        } catch (IllegalMove | IndexOutOfBoundsException | IllegalArgumentException | NotEnoughCoins | NotOnBoardException e) {
            viewMap.get(message.getSenderPlayer()).showError(e.getMessage(), EXPERT_ERROR);
            viewMap.get(message.getSenderPlayer()).chooseExpertCard();
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
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if(event.getPropertyName().equals("UpdateCloud")) {
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), turnLogic.getActivePlayer().getNickname(), game.getExpertsCard(), game.getPlayerByNickName(nickName).getNumOfCoin() );
            }
        }

        if(event.getPropertyName().contains("UpdateNode")) {

//            String value = event.getPropertyName();
//            String intValue = value.replaceAll("\\D+","");
//            int nodeID = Integer.parseInt(intValue);

            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), turnLogic.getActivePlayer().getNickname(), game.getExpertsCard(), game.getPlayerByNickName(nickName).getNumOfCoin());
            }
        }

        if(event.getPropertyName().equals("UpdateTeacher")) {
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), turnLogic.getActivePlayer().getNickname(), game.getExpertsCard(), game.getPlayerByNickName(nickName).getNumOfCoin());
            }
        }

        if(event.getPropertyName().equals("Merge")) {

            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), turnLogic.getActivePlayer().getNickname(), game.getExpertsCard(), game.getPlayerByNickName(nickName).getNumOfCoin());
            }
        }

        if(event.getPropertyName().contains("UpdateCoin-")) {

            String value = event.getPropertyName();
            String playerName = value.substring(11);
            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).newCoin(playerName, game.getPlayerByNickName(playerName).getNumOfCoin());
            }
        }

        if(event.getPropertyName().contains("UpdateBoard")) {
//            String value = event.getPropertyName();
//            String playerName = value.substring(11);
//
//            if(!viewMap.containsKey(playerName))
//                System.out.println("Non funziona la lettura da stringa");

            for (String nickName : viewMap.keySet()) {
                viewMap.get(nickName).worldUpdate(generateGameFieldMap(), game.getCloudTiles(), generateBoardMap(), turnLogic.getActivePlayer().getNickname(), game.getExpertsCard(), game.getPlayerByNickName(nickName).getNumOfCoin());
            }
        }
    }

    public void showGameInfo(Message messageReceived, String senderPlayer) {

        if(messageReceived.getType() == SHOW_CLOUD)
            viewMap.get(senderPlayer).showClouds(game.getCloudTiles());


        if(messageReceived.getType() == SHOW_BOARD) {
            Map<String, Board> boardMap = new HashMap<>();

            for(Player currentPlayer : game.getPlayersList()) {
                boardMap.put(currentPlayer.getNickname(), currentPlayer.getBoard());
            }

            viewMap.get(senderPlayer).showBoard(boardMap);
        }

        if(messageReceived.getType() == ASSISTANT_INFO)
            viewMap.get(senderPlayer).showAssistant(game.getPlayerByNickName(senderPlayer).getDeck().getRemainingCards());

        if(messageReceived.getType() == LAST_ASSISTANT) {
            Map<String, AssistantCard> lastCardMap = new HashMap<>();

            for(Player currentPlayer : game.getPlayersList()) {
                if(currentPlayer.getDeck().getLastCard() != null)
                    lastCardMap.put(currentPlayer.getNickname(), currentPlayer.getDeck().getLastCard());
            }

            viewMap.get(senderPlayer).showLastUsedCard(lastCardMap);
        }

        if(messageReceived.getType() == GAME_FIELD)
            viewMap.get(senderPlayer).showGameField(generateGameFieldMap());

        if(messageReceived.getType() == GET_COINS) {
            viewMap.get(senderPlayer).newCoin(senderPlayer, game.getPlayerByNickName(senderPlayer).getNumOfCoin());
        }

        if(messageReceived.getType() == EXPERT_CARD_REQ) {
            viewMap.get(senderPlayer).showExpertCards(game.getExpertsCard(), game.getPlayerByNickName(senderPlayer).getNumOfCoin());
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

    private Map<Integer, Node> generateGameFieldMap() {
        Map<Integer, Node> gameFieldMap = new HashMap<>();
        for (int i = 1; i <= game.getGameField().size(); i++) {
            gameFieldMap.put(i, game.getGameField().getIslandNode(i));
        }
        return gameFieldMap;
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
    /*
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
    }*/


    public Map<String, Board> generateBoardMap() {

        Map<String, Board> boardMap = new HashMap<>();
        for(Player currentPlayer : game.getPlayersList())
            boardMap.put(currentPlayer.getNickname(), currentPlayer.getBoard());
        return boardMap;
    }
}
