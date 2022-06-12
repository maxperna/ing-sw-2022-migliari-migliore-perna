package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.*;
import it.polimi.ingsw.observer.Listener;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.network.messages.ErrorType.*;

/**Class implementing the part of controller on the client, used to communicate with the client view
 * @author Massimo*/
public class ClientController implements ViewListener, Listener {

    private final View view;
    private Client client;
    private String nickname;
    private final ExecutorService actionQueue;
    private GameState phase;
    private int endTurnCounter;   //regulate the final phase of the action phase
    private int actionCounter;

    private int numOfPlayers;

    public ClientController(View view){
        this.view = view;
        this.actionQueue = Executors.newSingleThreadExecutor();
        this.phase = GameState.PREPARATION_PHASE;
        this.endTurnCounter = 1;
    }

    /**Method handling the connection information to create a client-server connection
     * @param connectionInfo hashMap containing server ip address and related port number
     */
    @Override
    public void connectionRequest(HashMap<String, String> connectionInfo){
        try {
            this.client = new ClientSocket(connectionInfo.get("address"), Integer.parseInt(connectionInfo.get("port")));
            client.receiveMessage();
            client.addListener(this);
        } catch (IOException e) {
            view.showError("Connection failed. Try a different address/port", CONNECTION_ERROR);
            view.connectionRequest();
        }
    }

    /**Method to send the nickname of the player
     * @param nickname chosen nickname*/
    @Override
    public void sendNickname(String nickname){
        this.nickname = nickname;
        client.sendMessage(new LoginInfo(nickname));
    }

    /**Method to send the param of a game to the server
     * @param numOfPlayers number of players of the game
     * @param expertMode chosen game mode*/
    @Override
    public void sendGameParam(int numOfPlayers, boolean expertMode){
        client.sendMessage(new GameParamMessage(this.nickname,numOfPlayers,expertMode));
    }

    /***/
    @Override
    public void getBoards(){
        client.sendMessage(new BoardInfoRequest(this.nickname));
    }

    /**Method to select every object could be recognized by an ID (i.e. island, cloud tiles)
     * @param ID selected object ID*/
    @Override
    public void chooseCloudTile(int ID){
        client.sendMessage(new GetCloudsMessage(nickname,ID));
    }


    /**Method to pick one single student on the view and move it on the island
     * @param student picked student
     * @param nodeID node target of the movement*/
    @Override
    public void moveStudentToIsland(Color student,int nodeID){
        decreaseActionCounter();
        client.sendMessage(new MovedStudentToIsland(nickname,student,nodeID));
    }

    /**Method to pick one single student on the view and move it into the dinner room
     * @param student picked student*/
    @Override
    public void moveStudentToDinner(Color student){
        decreaseActionCounter();
        client.sendMessage(new MovedStudentToBoard(nickname,student));}

    /**Method to select multiple students on the view
     * @param students selected students*/
    public void selectStudentArray(ArrayList<Color> students){}

    /**Method used to play an assistant card
     * @param playedCard selected card to play*/
    @Override
    public void playAssistantCard(int playedCard){
         client.sendMessage(new PlayAssistantMessage(nickname,playedCard));
    }

    @Override
    public void cloudsRequest() {
        client.sendMessage(new ChargedCloudsRequest(nickname));
    }

    @Override
    public void moveMotherNature(int numberOfSteps){
        client.sendMessage(new MoveMotherNatureMessage(nickname,numberOfSteps));
    }


    /**Method to play an expert card
     **/
    public void getExpertsCard(){
        client.sendMessage(new ExpertCardRequest(nickname));
    }

    @Override
    public void getGameField(){
        client.sendMessage((new GameFieldRequest(nickname)));
    }

    //EXPERT CARD METHODS
    public void playExpertCard1(int cardID){
        client.sendMessage(new PlayExpertCard1(nickname,cardID));
    }

    public void playExpertCard2(int cardID,int nodeID){
        client.sendMessage(new PlayExpertCard2(nickname,nodeID,cardID));
    }

    public void playExpertCard3(int nodeID,Color student,int cardID){
        client.sendMessage(new PlayExpertCard3(nickname,nodeID,student,cardID));
    }

    public void playExpertCard4(Color student,int cardID){
        client.sendMessage(new PlayExpertCard4(nickname,student,cardID));
    }

    public void playExpertCard5(ArrayList<Color> student1, ArrayList<Color> student2,int cardID){
        client.sendMessage(new PlayExpertCard5(nickname,student1,student2,cardID));
    }

    /**Method to get deck info*/
    public void requestAssistants(){
        client.sendMessage(new AssistantInfoMessage(nickname));
    }

    public void requestPlayedAssistants() {
        client.sendMessage(new LastCardRequest(nickname));
    }


    /**Sub state machine catching the type of experts to play get reading expertCardReply type
     * @param cardToPlay chosen card to play*/
    public void applyExpertEffect(ExpertCard cardToPlay){
        ExpertID typeOfExpert = cardToPlay.getExpType();
        switch (typeOfExpert){        //update with method of the CLI
            case COLOR:
            case TWO_LIST_COLOR:
            case NODE_ID_COLOR:
            case USER_ONLY:
            case NODE_ID:
            default:
        }
    }

    /*Aggiungere metodi per inviare messaggi
    * client.sendMessage(Message)*/

    /**Method handling the action on the base of the received message
     * @param receivedMessage message received from the server*/
    @Override
    public synchronized void catchAction(Message receivedMessage){

        switch (receivedMessage.getType()){
            case GAMEPARAM_REQUEST:
                actionQueue.execute(view::askGameParam);
                break;
            case NICK_REQ:
                actionQueue.execute(view::askPlayerNickname);
                break;
            case REMAINING_ITEM:
                RemainingItemReply answer = (RemainingItemReply) receivedMessage;
                actionQueue.execute(()->view.showRemainingTowerAndDeck(answer.getRemainingTowers(),answer.getReamingDecks()));
                break;
            case NUMBER_PLAYERS:
                NumberOfPlayersMessage numberOfPlayersMessage = (NumberOfPlayersMessage) receivedMessage;
                numOfPlayers = numberOfPlayersMessage.getNumberOfPlayers();
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currPlayer = (CurrentPlayerMessage) receivedMessage;
                switch(currPlayer.getCurrentState()){
                    case PREPARATION_PHASE:
                        phase = GameState.PREPARATION_PHASE;
                        actionQueue.execute(view::chooseAction);
                        break;
                    case ACTION_PHASE:
                        phase = GameState.ACTION_PHASE;
                        actionCounter = resetCounter();
                        endTurnCounter = 1;
                        actionQueue.execute(view::ActionPhaseTurn);
                        break;

                }break;

            case SHOW_CLOUD:
                UpdateCloudsMessage cloudsMessage = (UpdateCloudsMessage) receivedMessage;
                actionQueue.execute(()->view.showClouds(cloudsMessage.getChargedClouds()));
                if (endTurnCounter == 0 && actionCounter == 0) {
                    actionQueue.execute(() -> view.chooseCloudTile(cloudsMessage.getChargedClouds().size()));
                    endTurnCounter--;
                    actionQueue.execute(() -> view.showGenericMessage("Turn ended, waiting for other players"));
                }
                break;
            case UPDATE_COIN:
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
                actionQueue.execute(()->view.showAssistant(assistantsInfo.getDeck()));
                break;
            case SHOW_BOARD:
                if(phase.equals(GameState.PREPARATION_PHASE)){
                    Map<String, Board> boardMap = ((BoardInfoMessage)receivedMessage).getBoardMap();
                    boardMap.remove(nickname);
                    actionQueue.execute(() -> view.showBoard(boardMap));
                    actionQueue.execute(view::chooseAction);
                }
                else {
                    BoardInfoMessage boardInfo = (BoardInfoMessage) receivedMessage;
                    actionQueue.execute(() -> view.showBoard(boardInfo.getBoardMap()));
                }
                break;
            case GENERIC:
                GenericMessage message = (GenericMessage) receivedMessage;
                actionQueue.execute(()->view.showGenericMessage(message.getBody()));
                break;
                //case PREPARATION_PHASE
            case EXPERT_CARD_REPLY:
                //POSIZIONARE METODO VIEW
                break;
            case LAST_ASSISTANT:
                LastCardMessage lastCardMessage = (LastCardMessage)  receivedMessage;
                actionQueue.execute(()->view.showLastUsedCard(lastCardMessage.getLastCardMap()));
                requestAssistants();
                break;
            case GAME_FIELD:
                GameFieldMessage gameField = (GameFieldMessage) receivedMessage;
                actionQueue.execute(()->view.showGameField(gameField.getGameFieldMap()));
                break;
            case ERROR:
                ErrorMessage error = (ErrorMessage) receivedMessage;
                actionQueue.execute(()->view.showError(error.getErrorMessage(), error.getTypeError()));
                if(error.getTypeError() == LOGIN_ERROR) {
                    actionQueue.execute(view::askPlayerNickname);
                }
                //Action phase error handling
                if(error.getTypeError() == STUDENT_ERROR ) {
                    actionCounter ++;
                    actionQueue.execute(view::ActionPhaseTurn);
                }
                if(error.getTypeError() == ASSISTANT_ERROR) {
                    requestAssistants();
                }
                if(error.getTypeError() == MOTHER_NATURE_ERROR) {
                    endTurnCounter++;
                    actionQueue.execute(view::moveMotherNature);
                    endTurnCounter--;
                }
                if(error.getTypeError() == CLOUD_ERROR) {
                    actionQueue.execute(this::cloudsRequest);
                    endTurnCounter ++;
                }

                break;
            case WORLD_CHANGE:
                WorldChangeMessage worldChange = (WorldChangeMessage) receivedMessage;
                actionQueue.execute(()-> defaultViewLayout(worldChange));
                if(phase.equals(GameState.ACTION_PHASE) && worldChange.getCurrentPlayer().equals(nickname)) {
                    if(actionCounter > 0) {
                        actionQueue.execute(view::ActionPhaseTurn);
                    }
                    else if(actionCounter == 0 && endTurnCounter==1){
                        //MN movement
                        actionQueue.execute(view::moveMotherNature);
                        endTurnCounter--;
                    }
                    else if (endTurnCounter == 0 && actionCounter == 0) {
                        actionQueue.execute(() -> view.chooseCloudTile(worldChange.getChargedClouds().size()));
                        endTurnCounter--;
                        actionQueue.execute(() -> view.showGenericMessage("Turn ended, waiting for other players"));
                    }
                }

                break;
            /*CASE TYPE:
            *   richiedere un aggiornamento alla view*/
        }
    }

    @Override
    public void chooseTowerColorAndDeck(TowerColor color, DeckType deck) {
        client.sendMessage(new CreatePlayerMessage(nickname, color, deck));
    }

    @Override
    public void chooseDestination(String destination) {

    }

    @Override
    public void getPlayerInfo(String player) {

    }

    @Override
    public void update(Message message) {
        catchAction(message);
    }

    @Override
    public void chooseAction(int i) {
        switch (i){
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

    /**method to set the maximum num of movements depending on num of players*/
    private int resetCounter(){
        if(numOfPlayers == 3)
            return 4;
        else
            return 3;
    }

    /**Method to decrease the action counter for each action requiring it*/
    private void decreaseActionCounter(){
        actionCounter--;
    }
    private void defaultViewLayout(WorldChangeMessage message) {

        ((Cli)view).clearCli();
        view.showGameField(message.getGameFieldMap());
        view.showClouds(message.getChargedClouds());
        view.printBoard(message.getBoardMap().get(nickname), nickname);
    }

}
