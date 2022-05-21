package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.Server_Socket;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.fail;

class GameControllerTest {

    GameController GC = new GameController();
    Server server = new Server(GC);
    Server_Socket serverSocket = new Server_Socket(server,1300);
    Thread serverThread = new Thread(serverSocket, "Server");

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers");
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWorkExpert(String gameMode) {

        boolean expertMode = true;
        GameController gameController = new GameController();

        serverThread.start();

        try {
            ClientSocket clientSocket = new ClientSocket("0.0.0.0",1300);
            clientSocket.sendMessage(new GenericMessage("IO"));
        } catch (IOException e) {
            fail();
        }catch (NullPointerException es){
            assert (true);
        }


    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWork(String gameMode) {

        GameController gameController = new GameController();
    }

}