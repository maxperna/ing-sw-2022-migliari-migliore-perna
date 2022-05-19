package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.client_messages.*;
import it.polimi.ingsw.network.messages.client_messages.ExpertMessages.*;
import it.polimi.ingsw.network.messages.server_messages.ExpertCardReply;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**Class implementing the part of controller on the client, used to communicate with the client view
 * @author Massimo*/
public class ClientController {

    private final View view;
    private Client client;
    private String nickname;

    private final ExecutorService actionQueue;

    private ArrayList<ExpertID> expertsOnField = new ArrayList<>();
    private int maximumMNStep;       //used to check directly if a MN movement is legit

    public ClientController(View view){
        this.view = view;
        this.actionQueue = Executors.newSingleThreadExecutor();
    }

    /**Method handling the connection information to create a client-server connection
     * @param connectionInfo hashMap containing server ip address and related port number
     * @throws IOException if ClientSocket gives errors*/
    public void connectionRequest(HashMap<String,String> connectionInfo){
        try {
            this.client = new ClientSocket(connectionInfo.get("address"),Integer.getInteger(connectionInfo.get("port")));
            client.receiveMessage();
            //aggiunta observer
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
    public void playAssistantCard(AssistantCard playedCard){
        maximumMNStep = playedCard.getMotherNatureControl();
         client.sendMessage(new AssistantCardMessage(nickname,playedCard));
    }

    public void moveMotherNature(int numberOfSteps){
        if(numberOfSteps>maximumMNStep)
            return;

            //Show error message
        else
            client.sendMessage(new MoveMotherNatureMessage(nickname,numberOfSteps));
    }

    /**Method to play an expert card
     * @param cardID id of the card on the field*/
    public void chooseExpertCard(int cardID){
        client.sendMessage(new ExpertCardRequest(nickname,cardID));
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

    /**Sub state machine catching the type of experts to play get reading expertCardReply type
     * @param cardID choosen card to play*/
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
            case GAMEPARAM:
                //send game parameters
                break;
            case ENDLOGIN:
                //notify on view the end of the login phase
                break;
            case GET_EXPERT_PARAM:
                //notify the view requiring the parameters based on the type
                break;
            case NOTIFY_MERGE:
                //notify the merging of the island
                break;
            case NOTIFY_VICTORY:
                //end of the game, showing winning player
                break;
            case GENERIC:
                //show generic message
                break;
                //case PREPARATION_PHASE
            case EXPERT_CARD_REPLY:
                expertsOnField = ((ExpertCardReply)receivedMessage).getExpertID();

            /*CASE TYPE:
            *   richiedere un aggiornamento alla view*/
        }
    }
}
