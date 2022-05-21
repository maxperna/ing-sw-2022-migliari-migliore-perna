package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.client_messages.LoginInfo;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.Server_Socket;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController GC = new GameController();
    Server server = new Server(GC);
    Server_Socket serverSocket = new Server_Socket(server,1300);
    Thread serverThread = new Thread(serverSocket, "Server");

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers");
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWorkExpert(String gameMode) {

        String beppe = "Beppe";
        String gino = "Gino";
        boolean expertMode = true;
        GameController gameController = new GameController();

        serverThread.start();

        try {
            ClientSocket clientSocket = new ClientSocket("0.0.0.0",1300);
            clientSocket.sendMessage(new LoginInfo(beppe));
            clientSocket.sendMessage(new GameParamMessage(beppe, 2, expertMode));

            assertNotNull(gameController.getGame());
            assertEquals(1, gameController.getViewMap().size());
        } catch (IOException e) {
            fail();
        }


    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWork(String gameMode) {

        GameController gameController = new GameController();
    }

}