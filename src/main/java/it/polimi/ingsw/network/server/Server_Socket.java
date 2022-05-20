package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            Server.LOGGER.info("Server started on port "+ port + " " +socketServer.getInetAddress().getHostAddress());
        }catch (IOException e){
            Server.LOGGER.severe("Error starting server");
        }

        while(!Thread.currentThread().isInterrupted()){
            try{
                Socket client = socketServer.accept();
                client.setSoTimeout(5000);

                ClientHandler clientHandler = new ClientHandler(this,client);
                Thread clientThread = new Thread(clientHandler,"handler"+clientHandler.getClass().getName());
                clientThread.start();

            }catch(IOException e){
                Server.LOGGER.severe("Problem accepting connection");
            }
        }
    }



    /**Method to initialize connection with the server using only nickname in order to generate the linked virtual view
     * @param nickname nickname chosen
     * @param clientHandler client handler associated to the player*/
    public void addClient(String nickname,ClientHandler clientHandler){
        server.addClient(nickname,clientHandler);
    }


    /**Method used to notify the reception of a message from the client
     * @param receivedMessage message received from the client*/
    public void receiveMessage(Message receivedMessage){
        server.receivedMessage(receivedMessage);
    }


    public void disconnectClient(ClientHandler clientHandler) {
    }
}
