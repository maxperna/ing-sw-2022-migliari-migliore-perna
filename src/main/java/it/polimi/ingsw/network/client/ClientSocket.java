package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class implementing client method to send and receive message from the server using multithreading
 *
 * @author Massimo
 */

public class ClientSocket extends Client {
    private final Socket socket;

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final ExecutorService readExecutionQueue;     //gestione thread in lettura

    private final int SOCKET_TIMEOUT = 1000;

    /**
     * Constructor to initialize client socket and a new queue of executed task
     *
     * @param address IP address of the server
     * @param port    port on the server
     * @throws IOException if the socket can't connect client and server
     */

    public ClientSocket(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * Method to receive a message from the server
     */
    @Override
    public void receiveMessage() {
        readExecutionQueue.execute(() -> {
            while (!readExecutionQueue.isShutdown()) {
                //message received by the server
                try {
                    Message messageReceived = (Message) inputStream.readObject();          //reading the message
                    // Client.LOGGER.info("Received");
                    notifyListener(messageReceived);
                } catch (IOException | ClassNotFoundException e) {
                    disconnect();
                    readExecutionQueue.shutdownNow();       //closing the execution queue
                }

            }
        });
    }

    /**
     * Method to send a message to the client
     *
     * @param messageToSend message to be sent to the server
     */
    @Override
    public void sendMessage(Message messageToSend) {
        try {
            outputStream.writeObject(messageToSend);
            outputStream.reset();
        } catch (IOException e) {
            disconnect();
            //aggiungere observer
        }
    }

    /**
     * Method to disconnect from the server
     */
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                readExecutionQueue.shutdownNow();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Aggiungere observer
        }
    }
}
