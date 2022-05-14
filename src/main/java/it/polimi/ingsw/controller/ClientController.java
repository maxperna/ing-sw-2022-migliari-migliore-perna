package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**Class implementing the part of controller on the client, used to communicate with the client view
 * @author Massimo*/
public class ClientController {

    private final View view;
    private Client client;

    private final ExecutorService actionQueue;

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

    /*Aggiungere metodi per inviare messaggi
    * client.sendMessage(Message)*/

    /**Method handling the action on the base of the received message
     * @param receivedMessage message received from the server*/
    public void catchAction(Message receivedMessage){
        switch (receivedMessage.getType()){
            /*CASE TYPE:
            *   richiedere un aggiornamento alla view*/
        }
    }
}
