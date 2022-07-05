package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.server_messages.NicknameRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class used to create a socket that will receive the messages from the clients
 */
public class Server_Socket implements Runnable {
    private final Server server;
    private final int port;
    ServerSocket socketServer;

    /**
     * Default constructor
     *
     * @param server  is the server that will be listening on the chosen port
     * @param portNum is the id of the port that will be used to handle the connection
     */
    public Server_Socket(Server server, int portNum) {
        this.server = server;
        this.port = portNum;
    }

    /**
     * Method used to create the server socket and start the thread handling the connection
     */
    @Override
    public void run() {
        try {
            socketServer = new ServerSocket(port, 4, InetAddress.getLocalHost());
            Server.LOGGER.info("Server started on port " + port + " " + socketServer.getInetAddress().getHostAddress());
        } catch (IOException e) {
            Server.LOGGER.severe("Error starting server");
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = socketServer.accept();


                ClientHandler clientHandler = new ClientHandler(this, client);

                clientHandler.sendMessage(new NicknameRequest());
                Thread clientThread = new Thread(clientHandler, "handler" + clientHandler.getClass().getName());
                clientThread.start();

            } catch (IOException e) {
                Server.LOGGER.severe("Problem accepting connection");
            }
        }
    }


    /**
     * Method to initialize connection with the server using only nickname in order to generate the linked virtual view
     *
     * @param nickname      nickname chosen
     * @param clientHandler client handler associated to the player
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        server.addClient(nickname, clientHandler);
    }


    /**
     * Method used to notify the reception of a message from the client
     *
     * @param receivedMessage message received from the client
     */
    public void receiveMessage(Message receivedMessage) {
        server.receivedMessage(receivedMessage);
    }

    public void disconnect(ClientHandler CH){
        server.disconnect(CH);
    }

}
