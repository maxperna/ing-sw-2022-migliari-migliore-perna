package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;

public class Server_Socket implements Runnable{
    private final Server server;
    ServerSocket socketServer;
    private final int port;

    public Server_Socket(Server server, int portNum){
        this.server = server;
        this.port = portNum;
    }

    @Override
    public void run() {
        try {
            socketServer = new ServerSocket(port);
            Server.LOGGER.info("Server started on port"+ port);
        }catch (IOException e){
            Server.LOGGER.severe("Error starting server");
        }

        while(!Thread.currentThread().isInterrupted()){
//            try{}catch{}
        }
    }



    /**Method to initialize connection with the server using only nickname in order to generate the linked virtual view
     * @param nickname nickname chosen
     * @param clientHandler client handler associated to the player*/
    public void addClient(String nickname,ClientHandler clientHandler){
        server.addClient(nickname,clientHandler);
    }

    /**Method used to add a new player to the game
     * @param assistant chosen assistant at the login
     * @param towerColor chosen tower color at the login*/
    public void addPlayer(DeckType assistant, TowerColor towerColor){}

    /**Method used to notify the reception of a message from the client
     * @param receivedMessage message received from the client*/
    public void receiveMessage(Message receivedMessage){}

    /**Method used to disconnect a client from the sercer
     * @param clientToDisconnect client to disconnect from the server*/
    public void disconnectClient(ClientHandler clientToDisconnect){}
}
