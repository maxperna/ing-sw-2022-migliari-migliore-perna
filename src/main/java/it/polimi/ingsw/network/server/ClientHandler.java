package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**Class representing the virtual client on the server
 * @author Massimo*/
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server_Socket serverSocket;

    //IO STREAM
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //STREAM LOCKER
    private final Object inputLock;
    private final Object outputLock;

    private boolean connected;

    public ClientHandler(Server_Socket serverSocket, Socket clientSocket) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.connected = true;

        //Locker creation
        this.inputLock = new Object();
        this.outputLock = new Object();

        try {
            this.output = new ObjectOutputStream(clientSocket.getOutputStream());
            this.input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            Server.LOGGER.severe((e.getMessage()));
        }
    }


    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe(clientSocket.getInetAddress() + "connection dropped");
            disconnect();
        }
    }

    /**
     * Client messages listening handler
     *
     * @throws IOException if there are problem with the socket
     */
    private void handleClientConnection() throws IOException {

        try {
            while (!Thread.currentThread().isInterrupted()) {
                //Synchronization on the input
                synchronized (this){
                    Message receivedMessage = (Message) input.readObject();
                    if (receivedMessage.getType() == MessageType.LOGIN)
                        serverSocket.addClient(receivedMessage.getSenderPlayer(), this);
//                  serverSocket.addClient(message.getNick,m.getColorTowe,m.getassistant,this);
                    else {
                        Server.LOGGER.info("Message received" + receivedMessage);
                        serverSocket.receiveMessage(receivedMessage);
                    }
                }
            }
        } catch (ClassNotFoundException e){
            Server.LOGGER.severe("Client input not valid");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        clientSocket.close();
    }

    /**
     * Client disconnection routine
     */
    public void disconnect() {
        if (connected) {
            try {
                if (!clientSocket.isClosed())
                    clientSocket.close();
            } catch (IOException e) {
                Server.LOGGER.severe(e.getMessage());
            }
            connected = false;
            Thread.currentThread().interrupt();

        }
    }

    /**
     * Method used to send message to the client
     *
     * @param messageToSend message I want to send to the client
     */
    public void sendMessage(Message messageToSend) {
        try {
            synchronized (outputLock){
                output.writeObject(messageToSend);
                output.reset();
                Server.LOGGER.info("Message sent " + messageToSend);
            }
        }catch(IOException e) {
            e.printStackTrace();
            Server.LOGGER.severe(e.getMessage());
            disconnect();
        }
    }
}

