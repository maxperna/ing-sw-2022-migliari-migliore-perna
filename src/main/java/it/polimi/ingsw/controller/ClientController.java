package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.*;
import it.polimi.ingsw.view.Listener;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**Class implementing the part of controller on the client, used to communicate with the client view
 * @author Massimo*/
public class ClientController implements ViewListener, Listener {

    private final View view;
    private Client client;
    private String nickname;

    private final ExecutorService actionQueue;

    private ArrayList<ExpertID> expertsOnField = new ArrayList<>();

    private final ArrayList<String> inGamePlayer = new ArrayList<>();    //list of others player nickname

    public ClientController(View view){
        this.view = view;
        this.actionQueue = Executors.newSingleThreadExecutor();
    }

    /**Method handling the connection information to create a client-server connection
     * @param connectionInfo hashMap containing server ip address and related port number
     */
    public void connectionRequest(HashMap<String, String> connectionInfo){
        try {
            this.client = new ClientSocket(connectionInfo.get("address"), Integer.parseInt(connectionInfo.get("port")));
            client.receiveMessage();
            client.addListener(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //Aggiornare la view dell'insuccesso della connessione
        }
    }

    /**Method to send the nickname of the player
     * @param nickname chosen nickname*/
    public void sendNickname(String nickname){
        this.nickname = nickname;
        client.sendMessage(new LoginInfo(nickname));
    }

    /**Method to send the param of a game to the server
     * @param numOfPlayers number of players of the game
     * @param expertMode chosen game mode*/
    public void sendGameParam(int numOfPlayers, boolean expertMode){
        client.sendMessage(new GameParamMessage(this.nickname,numOfPlayers,expertMode));
    }

    /**Method to select every object could be recognized by an ID (i.e. island, cloud tiles)
     * @param ID selected object ID*/
    public void sendSelectedID(int ID){
        client.sendMessage(new SelectionIDMessage(nickname,ID));
    }


    /**Method to pick one single student on the view and move it on the island
     * @param student picked student
     * @param nodeID node target of the movement*/
    public void moveStudentToIsland(Color student,int nodeID){
        client.sendMessage(new MovedStudentToIsland(nickname,student,nodeID));}

    /**Method to pick one single student on the view and move it into the dinner room
     * @param student picked student*/
    public void moveStudentToDinner(Color student){
        client.sendMessage(new MovedStudentToBoard(nickname,student));}

    /**Method to select multiple students on the view
     * @param students selected students*/
    public void selectStudentArray(ArrayList<Color> students){}

    /**Method used to play an assistant card
     * @param playedCard selected card to play*/
    public void playAssistantCard(int playedCard){
         client.sendMessage(new PlayAssistantMessage(nickname,playedCard));
    }

    public void moveMotherNature(int numberOfSteps){
        client.sendMessage(new MoveMotherNatureMessage(nickname,numberOfSteps));
    }

    /**Method to play an expert card
     * @param cardID id of the card on the field*/
    public void chooseExpertCard(int cardID){
        client.sendMessage(new ExpertCardRequest(nickname,cardID));
    }

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
        System.out.println("Richiesta assistente");
        client.sendMessage(new AssistantInfoMessage(nickname));
    }


    /**Sub state machine catching the type of experts to play get reading expertCardReply type
     * @param cardID chosen card to play*/
    public void applyExpertEffect(int cardID){
        ExpertID typeOfExpert = expertsOnField.get(cardID);
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
    public void catchAction(Message receivedMessage){
        switch (receivedMessage.getType()){
            case GAMEPARAM_REQUEST:
                actionQueue.execute(view::askGameParam);
                break;
            case REMAINING_ITEM:
                RemainingItemReply answer = (RemainingItemReply) receivedMessage;
                actionQueue.execute(()->view.showRemainingTowerAndDeck(answer.getRemainingTowers(),answer.getReamingDecks()));
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currPlayer = (CurrentPlayerMessage) receivedMessage;
                switch(currPlayer.getCurrentState()){
                    case PREPARATION_PHASE:
                        requestAssistants();
                        //preparation phase method
                        break;
                    case ACTION_PHASE:
                        actionQueue.execute(view::startActionPhase);

                        break;

                }break;

            case CHARGECLOUD:
                UpdateCloudsMessage cloudsMessage = (UpdateCloudsMessage) receivedMessage;
                actionQueue.execute(()->view.showClouds(cloudsMessage.getChargedClouds()));
                break;
            case UPDATE_COIN:
                break;
            case NOTIFY_MERGE:
                //notify the merging of the island
                break;
            case NOTIFY_VICTORY:
                //end of the game, showing winning player
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
                BoardInfoMessage boardInfo = (BoardInfoMessage)  receivedMessage;
//                actionQueue.execute(()->view.);
                break;
            case GENERIC:
                GenericMessage message = (GenericMessage) receivedMessage;
                actionQueue.execute(()->view.showGenericMessage(message.getBody()));
                break;
                //case PREPARATION_PHASE
            case EXPERT_CARD_REPLY:
                expertsOnField = ((ExpertCardReply)receivedMessage).getExpertID();
                break;
            case LAST_ASSISTANT:
                LastCardMessage lastCard = (LastCardMessage)  receivedMessage;
                if(lastCard.getLastCardMap().size()!=0){ }
                break;
            case UPDATE_NODE:
                break;
            case GAME_FIELD:
                GameFieldMessage gameField = (GameFieldMessage) receivedMessage;
                actionQueue.execute(()->view.showGameField(gameField.getGameFieldMap()));

                break;
            case ERROR:
                ErrorMessage error = (ErrorMessage) receivedMessage;
                actionQueue.execute(()->view.showError(error.getErrorMessage()));
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

}
