package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.*;
import it.polimi.ingsw.observer.Listener;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.network.messages.ErrorType.*;

/**
 * Class implementing the part of controller on the client, used to communicate with the client view
 *
 * @author Massimo
 */
public class ClientController implements ViewListener, Listener {

    private final View view;
    private final ExecutorService actionQueue;
    private Client client;
    private String nickname;
    private GameState phase;
    private ArrayList<ExpertCard> expertCardsOnField = new ArrayList<>();
    private boolean expertMode;
    private boolean expertPlayed;
    private boolean studentsMoved;
    private boolean movedMN;
    private boolean endTurn;
    private int numOfPlayers;

    /**
     * Default constructor
     *
     * @param view is the view associated to the ClientController
     */
    public ClientController(View view) {
        this.view = view;
        this.actionQueue = Executors.newSingleThreadExecutor();
        this.phase = GameState.PREPARATION_PHASE;
        this.expertPlayed = true;
        this.studentsMoved = false;
        this.movedMN = false;
        this.endTurn = false;
    }

    /**
     * Method handling the connection information to create a client-server connection
     *
     * @param connectionInfo hashMap containing server ip address and related port number
     */
    @Override
    public void connectionRequest(HashMap<String, String> connectionInfo) {
        try {
            this.client = new ClientSocket(connectionInfo.get("address"), Integer.parseInt(connectionInfo.get("port")));
            client.receiveMessage();
            client.addListener(this);
        } catch (IOException e) {
            view.showError("Connection failed. Try a different address/port", CONNECTION_ERROR);
            view.connectionRequest();
        }
    }

    /**
     * Method to send the nickname of the player
     *
     * @param nickname chosen nickname
     */
    @Override
    public void sendNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginInfo(nickname));
    }

    /**
     * Method to send the param of a game to the server
     *
     * @param numOfPlayers number of players of the game
     * @param expertMode   chosen game mode
     */
    @Override
    public void sendGameParam(int numOfPlayers, boolean expertMode) {
        this.expertMode = expertMode;
        client.sendMessage(new GameParamMessage(this.nickname, numOfPlayers, expertMode));
    }

    /**
     * Method used to create a board request
     */
    @Override
    public void getBoards() {
        client.sendMessage(new BoardInfoRequest(this.nickname));
    }

    /**
     * Method to select every object could be recognized by an ID (i.e. island, cloud tiles)
     *
     * @param ID selected object ID
     */
    @Override
    public void chooseCloudTile(int ID) {
        client.sendMessage(new GetCloudsMessage(nickname, ID));
    }


    /**
     * Method to pick one single student on the view and move it on the island
     *
     * @param student picked student
     * @param nodeID  node target of the movement
     */
    @Override
    public void moveStudentToIsland(Color student, int nodeID) {
        client.sendMessage(new MovedStudentToIsland(nickname, student, nodeID));
    }

    /**
     * Method that evaluates what request will be sent to the server based on message type received
     *
     * @param type is the type of the message received
     */
    @Override
    public void actionPhaseChoice(MessageType type) {
        if (type == MessageType.MOVE_TO_DINING || type == MessageType.MOVE_TO_ISLAND)
            client.sendMessage(new StudentsAvailableRequest(nickname, type));
        if (type == MessageType.EXPERT_CARD_REQ)
            client.sendMessage(new ExpertCardRequest(nickname));
    }

    /**
     * Method to pick one single student on the view and move it into the dinner room
     *
     * @param student picked student
     */
    @Override
    public void moveStudentToDinner(Color student) {
        client.sendMessage(new MovedStudentToBoard(nickname, student));
    }

    /**
     * Method used to play an assistant card
     *
     * @param playedCard selected card to play
     */
    @Override
    public void playAssistantCard(int playedCard) {
        client.sendMessage(new PlayAssistantMessage(nickname, playedCard));
    }

    /**
     * Method used to request clouds information to the server
     */
    @Override
    public void cloudsRequest() {
        client.sendMessage(new ChargedCloudsRequest(nickname));
    }

    /**
     * Method used to create a message about mother nature movement
     *
     * @param numberOfSteps is the number of steps that mother nature will perform
     */
    @Override
    public void moveMotherNature(int numberOfSteps) {
        endTurn = true;
        client.sendMessage(new MoveMotherNatureMessage(nickname, numberOfSteps));
    }

    /**
     * Method to play an expert card
     **/
    @Override
    public void getExpertsCard() {
        client.sendMessage(new ExpertCardRequest(nickname));
    }

    /**
     * Method used to send a gameField request to the server
     **/
    @Override
    public void getGameField() {
        client.sendMessage((new GameFieldRequest(nickname)));
    }

    /**
     * Method to play an expert card that requires no additional parameters
     *
     * @param cardID is  the ID [0,1,2] that refers to the card played
     **/
    @Override
    public void playExpertCard1(int cardID) {
        client.sendMessage(new PlayExpertCard1(nickname, cardID));
    }

    /**
     * Method to play an expert card that requires a node ID as parameter
     *
     * @param cardID is the ID [0,1,2] that refers to the card played
     * @param nodeID is the ID of the chosen island
     **/
    @Override
    public void playExpertCard2(int cardID, int nodeID) {
        client.sendMessage(new PlayExpertCard2(nickname, nodeID, cardID));
    }

    /**
     * Method to play an expert card that requires a node ID and a Color as parameters
     *
     * @param cardID  is the ID [0,1,2] that refers to the card played
     * @param nodeID  is the ID of the chosen island
     * @param student is the color chosen (can also refer to a student of that color, based on which expert has been played)
     **/
    @Override
    public void playExpertCard3(int cardID, int nodeID, Color student) {
        client.sendMessage(new PlayExpertCard3(nickname, nodeID, student, cardID));
    }

    /**
     * Method to play an expert card that requires a color as parameter
     *
     * @param cardID  is the ID [0,1,2] that refers to the card played
     * @param student is the color chosen (can also refer to a student of that color, based on which expert has been played)
     **/
    @Override
    public void playExpertCard4(int cardID, Color student) {
        client.sendMessage(new PlayExpertCard4(nickname, student, cardID));
    }

    /**
     * Method to play an expert card that requires two arrayLists of Color as parameters
     *
     * @param cardID   is the ID [0,1,2] that refers to the card played
     * @param student1 is the first arrayList of students, usually the ones that will be taken from an external source
     * @param student2 is the second arrayList of students, usually tha ones that will be moved to an external source
     **/
    @Override
    public void playExpertCard5(int cardID, ArrayList<Color> student1, ArrayList<Color> student2) {
        client.sendMessage(new PlayExpertCard5(nickname, student1, student2, cardID));
    }

    /**
     * Method to get deck info
     */
    public void requestAssistants() {
        client.sendMessage(new AssistantInfoMessage(nickname));
    }

    /**
     * Method used to get last played card
     */
    public void requestPlayedAssistants() {
        client.sendMessage(new LastCardRequest(nickname));
    }


    /**
     * Sub state machine catching the type of experts to play get reading expertCardReply type
     *
     * @param cardID chosen card to play
     */
    public void applyExpertEffect(int cardID) {
        ExpertID typeOfExpert = expertCardsOnField.get(cardID).getExpType();

        switch (typeOfExpert) {        //update with method of the CLI
            case USER_ONLY:
                this.playExpertCard1(cardID);
                break;
            case COLOR: {
                if (expertCardsOnField.get(cardID) instanceof Expert9)
                    actionQueue.execute(() -> view.playExpert9(cardID, (Expert9) expertCardsOnField.get(cardID)));
                else if (expertCardsOnField.get(cardID) instanceof Expert11)
                    actionQueue.execute(() -> view.playExpert11(cardID, (Expert11) expertCardsOnField.get(cardID)));
                else if (expertCardsOnField.get(cardID) instanceof Expert12)
                    actionQueue.execute(() -> view.playExpert12(cardID, (Expert12) expertCardsOnField.get(cardID)));
            }
            break;
            case TWO_LIST_COLOR: {
                if (expertCardsOnField.get(cardID) instanceof Expert7)
                    actionQueue.execute(() -> view.playExpert7(cardID, (Expert7) expertCardsOnField.get(cardID)));
                else if (expertCardsOnField.get(cardID) instanceof Expert10)
                    actionQueue.execute(() -> view.playExpert10(cardID, (Expert10) expertCardsOnField.get(cardID)));
            }
            break;
            case NODE_ID_COLOR: {
                if (expertCardsOnField.get(cardID) instanceof Expert1)
                    actionQueue.execute(() -> view.playExpert1(cardID, (Expert1) expertCardsOnField.get(cardID)));
            }
            break;
            case NODE_ID: {
                if (expertCardsOnField.get(cardID) instanceof Expert3)
                    actionQueue.execute(() -> view.playExpert3(cardID, (Expert3) expertCardsOnField.get(cardID)));
                else if (expertCardsOnField.get(cardID) instanceof Expert5)
                    actionQueue.execute(() -> view.playExpert5(cardID, (Expert5) expertCardsOnField.get(cardID)));
            }
            break;
        }
    }

    /**
     * Method used to get the number of coins of the user
     */
    public void getCoins() {
        client.sendMessage(new GetCoins(nickname));
    }


    /**
     * Method handling the action on the base of the received message
     *
     * @param receivedMessage message received from the server
     */
    @Override
    public synchronized void catchAction(Message receivedMessage) {

        switch (receivedMessage.getType()) {
            case GAME_PARAM_REQUEST:
                actionQueue.execute(view::askGameParam);
                break;
            case NICK_REQ:
                actionQueue.execute(view::askPlayerNickname);
                break;
            case REMAINING_ITEM:
                RemainingItemReply answer = (RemainingItemReply) receivedMessage;
                actionQueue.execute(() -> view.showRemainingTowerAndDeck(answer.getRemainingTowers(), answer.getReamingDecks()));
                break;
            case NUMBER_PLAYERS:
                NumberOfPlayersMessage numberOfPlayersMessage = (NumberOfPlayersMessage) receivedMessage;
                numOfPlayers = numberOfPlayersMessage.getNumberOfPlayers();
                expertMode = numberOfPlayersMessage.isExpertMode();
                break;
            case START_GAME:
                actionQueue.execute(view::startGame);
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currPlayer = (CurrentPlayerMessage) receivedMessage;
                switch (currPlayer.getCurrentState()) {
                    case PREPARATION_PHASE:
                        phase = GameState.PREPARATION_PHASE;
                        actionQueue.execute(view::chooseAction);
                        break;
                    case ACTION_PHASE:
                        phase = GameState.ACTION_PHASE;
                        if (expertMode)
                            expertPlayed = false;
                        studentsMoved = false;
                        movedMN = false;
                        endTurn = false;
                        actionQueue.execute(() -> view.actionPhaseTurn(expertPlayed, studentsMoved));
                        break;

                }
                break;

            case SHOW_CLOUD:
                UpdateCloudsMessage cloudsMessage = (UpdateCloudsMessage) receivedMessage;
                actionQueue.execute(() -> view.showClouds(cloudsMessage.getChargedClouds()));
                if (endTurn) {
                    actionQueue.execute(() -> view.chooseCloudTile(cloudsMessage.getChargedClouds().size()));
                    endTurn = false;
                    actionQueue.execute(() -> view.showGenericMessage("Turn ended, waiting for other players"));
                }
                break;
            case UPDATE_COIN:
                UpdateCoin coinMessage = (UpdateCoin) receivedMessage;
                actionQueue.execute(() -> view.newCoin(nickname, coinMessage.getCoinValue()));
                break;
            case NOTIFY_MERGE:
                //notify the merging of the island
                break;
            case NOTIFY_VICTORY:
                //end of the game, showing winning
                break;
            case END_GAME:
                break;
            case UPDATE_TEACHERS:
                break;
            case ASSISTANT_INFO:
                AssistantCardsMessage assistantsInfo = (AssistantCardsMessage) receivedMessage;
                actionQueue.execute(() -> view.showAssistant(assistantsInfo.getDeck()));
                break;
            case SHOW_BOARD:
                if (phase.equals(GameState.PREPARATION_PHASE)) {
                    Map<String, Board> boardMap = ((BoardInfoMessage) receivedMessage).getBoardMap();
                    boardMap.remove(nickname);
                    actionQueue.execute(() -> view.showBoard(boardMap));
                    actionQueue.execute(view::chooseAction);
                } else {
                    BoardInfoMessage boardInfo = (BoardInfoMessage) receivedMessage;
                    actionQueue.execute(() -> view.showBoard(boardInfo.getBoardMap()));
                }
                break;
            case GENERIC:
                GenericMessage message = (GenericMessage) receivedMessage;
                actionQueue.execute(() -> view.showGenericMessage(message.getBody()));
                break;
            //case PREPARATION_PHASE
            case EXPERT_CARD_REPLY:
                expertCardsOnField = ((ExpertCardReply) receivedMessage).getExpertID();
                actionQueue.execute(view::chooseExpertCard);
                break;
            case LAST_ASSISTANT:
                LastCardMessage lastCardMessage = (LastCardMessage) receivedMessage;
                actionQueue.execute(() -> view.showLastUsedCard(lastCardMessage.getLastCardMap()));
                requestAssistants();
                break;
            case STUDENT_REPLY:
                AvailableStudentsReply availableStudentsReply = (AvailableStudentsReply) receivedMessage;
                actionQueue.execute(() -> view.availableStudents(availableStudentsReply.getAvailableStudents(), availableStudentsReply.getTypeOfMovement(), availableStudentsReply.getIslandSize()));
                break;
            case GAME_FIELD:
                GameFieldMessage gameField = (GameFieldMessage) receivedMessage;
                actionQueue.execute(() -> view.showGameField(gameField.getGameFieldMap()));
                break;
            case ERROR:
                ErrorMessage error = (ErrorMessage) receivedMessage;
                actionQueue.execute(() -> view.showError(error.getErrorMessage(), error.getTypeError()));
                if (error.getTypeError() == LOGIN_ERROR) {
                    actionQueue.execute(view::askPlayerNickname);
                }
                //Action phase error handling
                if (error.getTypeError() == STUDENT_ERROR) {
                    actionQueue.execute(() -> view.actionPhaseTurn(expertPlayed, studentsMoved));
                }
                if (error.getTypeError() == ASSISTANT_ERROR) {
                    actionQueue.execute(this::requestAssistants);
                }
                if (error.getTypeError() == MOTHER_NATURE_ERROR) {
                    endTurn = false;
                    actionQueue.execute(view::moveMotherNature);

                }
                if (error.getTypeError() == CLOUD_ERROR) {
                    endTurn = true;
                    actionQueue.execute(this::cloudsRequest);
                }
                if (error.getTypeError() == EXPERT_ERROR) {
                    actionQueue.execute(this::getExpertsCard);
                }
                if(error.getTypeError() == CLOSED_CONNECTION){
                    actionQueue.execute(view::disconnect);
                }

                break;
            case WORLD_CHANGE:
                WorldChangeMessage worldChange = (WorldChangeMessage) receivedMessage;
                actionQueue.execute(() -> view.worldUpdate(worldChange.getGameFieldMap(), worldChange.getChargedClouds(), worldChange.getBoardMap(), nickname, worldChange.getCurrentPlayer(), worldChange.getExperts(), worldChange.getNumOfCoins()));
                break;
            case AVAILABLE_ACTION:
                AvailableActionMessage availableActionMessage = (AvailableActionMessage) receivedMessage;
                setBooleanControl(availableActionMessage.areAllStudentsMoved(), availableActionMessage.isMotherNatureMoved(), availableActionMessage.isExpertPlayed());
                System.out.println("allStudentMoved: " + studentsMoved + "- MotherNature: " + movedMN + "- ExpertPlayed: " + expertPlayed);
                if (!studentsMoved) {
                    actionQueue.execute(() -> view.actionPhaseTurn(expertPlayed, studentsMoved));
                } else if (!movedMN) {
                    if (expertMode)
                        actionQueue.execute(() -> view.moveMNplusExpert(expertPlayed));
                    else
                        actionQueue.execute(view::moveMotherNature);
                } else {
                    actionQueue.execute(() -> view.chooseCloudTile(numOfPlayers));
                }
        }
    }

    /**
     * Method used to create a message containing the TowerColor and DeckType chosen
     *
     * @param color is the towerColor
     * @param deck  is the deckType
     */
    @Override
    public void chooseTowerColorAndDeck(TowerColor color, DeckType deck) {
        client.sendMessage(new CreatePlayerMessage(nickname, color, deck));
    }

    /**
     * method used to read a message received from the objects observed
     *
     * @param message is the message received
     */
    @Override
    public void update(Message message) {
        catchAction(message);
    }

    /**
     * Method used to create a request that matches the player's action
     *
     * @param i is the value chosen by the player
     */
    @Override
    public void chooseAction(int i) {
        switch (i) {
            case 1:
                requestPlayedAssistants();
                break;
            case 2:
                getBoards();
                break;
            default:
                break;
        }
    }


    /**
     * Method used to create a request based on the player's choice
     *
     * @param expert_mode is a boolean that indicates if the expert mode is enabled or not (also used to lock the player from using more than an expert card)
     */
    public void askAction(Boolean expert_mode) {
        if (!studentsMoved)
            actionQueue.execute(() -> view.actionPhaseTurn(expertPlayed, studentsMoved));
        else
            actionQueue.execute(() -> view.moveMNplusExpert(expertPlayed));
    }


    @Override
    public void guiExpertShow(ArrayList<ExpertCard> expertCards, boolean expertPlayed, int numOfCoins) {
        Gui gui = (Gui) view;
        getExpertsCard();
        gui.showExpertCards(expertCards, expertPlayed, numOfCoins);
    }

    private void setBooleanControl(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed) {
        this.studentsMoved = allStudentsMoved;
        this.movedMN = motherNatureMoved;
        if (expertMode)
            this.expertPlayed = expertPlayed;
        else
            this.expertPlayed = true;
    }

}
