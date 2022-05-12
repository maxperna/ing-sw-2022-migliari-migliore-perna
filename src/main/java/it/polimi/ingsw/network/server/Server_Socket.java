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

    /**Method used to login a new player
     * @param nickname nickname of the new player
     * @param assistant chosen assistant at the login
     * @param towerColor chosen tower color at the login
     * @param clientHandler associated client handler*/
    public void addClient(String nickname, DeckType assistant, TowerColor towerColor, ClientHandler clientHandler){}

    /**Method used to notify the reception of a message from the client
     * @param reiceivedMessage message received from the client*/
    public void receiveMessage(Message reiceivedMessage){}

    /**Method used to disconnect a client from the sercer
     * @param clientToDisconnect client to disconnect from the server*/
    public void disconnectClient(ClientHandler clientToDisconnect){}
}
