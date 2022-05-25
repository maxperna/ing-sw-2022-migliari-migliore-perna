package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.Server_Socket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.fail;


public class ConnectionTest {

    GameController GC = new GameController();
    Server server = new Server(GC);
    Server_Socket serverSocket = new Server_Socket(server,1300);
    Thread serverThread = new Thread(serverSocket, "Server");

    @DisplayName("Connection test")
    @Test
    void serverCreation(){

        serverThread.start();

        Socket clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress("0.0.0.0",1300));
            if(clientSocket.isConnected())
                assert (true);
            else
                fail();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @DisplayName("Message test")
    @Test

    void messageReceptionTest(){
        serverThread.start();

        try {
            ClientSocket clientSocket = new ClientSocket("0.0.0.0",1300);
            clientSocket.sendMessage(new GenericMessage("Test Connessione"));
        } catch (IOException e) {
            fail();
        }catch (NullPointerException es){
            assert (true);
        }


    }
}
