package it.polimi.ingsw.controller;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.AssistantCardMessage;
import it.polimi.ingsw.network.messages.client_messages.LoginInfo;
import it.polimi.ingsw.network.messages.client_messages.MoveMotherNatureMessage;
import it.polimi.ingsw.network.messages.client_messages.SelectionIDMessage;
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

    /**Metod to pick one single student on the view
     * @param student picked student*/
    public void selectStudent(Color student){}

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

            /*CASE TYPE:
            *   richiedere un aggiornamento alla view*/
        }
    }
}
