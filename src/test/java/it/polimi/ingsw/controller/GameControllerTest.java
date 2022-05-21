package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.client_messages.CreatePlayerMessage;
import it.polimi.ingsw.network.messages.client_messages.LoginInfo;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import it.polimi.ingsw.network.messages.server_messages.GenericMessage;
import it.polimi.ingsw.network.messages.server_messages.PlayerInitMessage;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.Server_Socket;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers");
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWorkExpert(String gameMode) {

        GameController gameController = new GameController();

        String beppe = "Beppe";
        String gino = "Gino";
        boolean expertMode = true;

        gameController.loginForTest(beppe);
        gameController.onMessageReceived(new GameParamMessage(beppe, 2, expertMode));
        gameController.loginForTest(gino);


        assertEquals(2, gameController.getViewMap().size());
        assertNotNull(gameController.getGame());
        assertEquals(2, gameController.getGame().NUM_OF_PLAYERS);

        gameController.onMessageReceived(new CreatePlayerMessage(beppe, TowerColor.BLACK, DeckType.SAGE));

        assertEquals(beppe, gameController.getGame().getPlayersList().get(0).getNickname());
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWork(String gameMode) {

        GameController gameController = new GameController();
    }

}