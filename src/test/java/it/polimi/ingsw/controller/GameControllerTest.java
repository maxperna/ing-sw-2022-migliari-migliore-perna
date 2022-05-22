package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.client_messages.AssistantCardMessage;
import it.polimi.ingsw.network.messages.client_messages.ChargedCloudsRequest;
import it.polimi.ingsw.network.messages.client_messages.CreatePlayerMessage;
import it.polimi.ingsw.network.messages.server_messages.GameParamMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.ArrayList;
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
        gameController.onMessageReceived(new CreatePlayerMessage(gino, TowerColor.WHITE, DeckType.WITCH));
        assertEquals(beppe, gameController.getGame().getPlayersList().get(0).getNickname());
        assertEquals(gino, gameController.getGame().getPlayersList().get(1).getNickname());
        assertEquals(TowerColor.BLACK, gameController.getGame().getPlayersList().get(0).getTowerColor());
        assertEquals(TowerColor.WHITE, gameController.getGame().getPlayersList().get(1).getTowerColor());
        assertEquals(DeckType.SAGE, gameController.getGame().getPlayersList().get(0).getDeckType());
        assertEquals(DeckType.WITCH, gameController.getGame().getPlayersList().get(1).getDeckType());

        for(CloudTile currentTile : gameController.getGame().getCloudTiles()) {
            assertEquals(0, currentTile.getStudents().size());
        }

        System.out.println("\nThe List Is:");
        ArrayList<Player> playerOrder = new ArrayList<>();
        for(Player currentPlayer : gameController.getTurnLogic().getPlayersOrders()) {
            System.out.println(currentPlayer.getNickname());
            playerOrder.add(currentPlayer);
        }

        String currentPlayer = gameController.getTurnLogic().getActivePlayer().getNickname();
        System.out.println("The current player is: " + currentPlayer);
        gameController.onMessageReceived(new ChargedCloudsRequest(currentPlayer));

        for(CloudTile currentTile : gameController.getGame().getCloudTiles()) {
            assertEquals(3, currentTile.getStudents().size());
        }
        gameController.onMessageReceived(new AssistantCardMessage(playerOrder.get(0).getNickname(), gameController.getGame().getPlayerByNickName(currentPlayer).getDeck().getDeck().get(0)));

        gameController.onMessageReceived(new AssistantCardMessage(playerOrder.get(1).getNickname(), gameController.getGame().getPlayerByNickName(currentPlayer).getDeck().getDeck().get(1)));

        System.out.println("The current player is: " + gameController.getTurnLogic().getActivePlayer().getNickname());


    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWork(String gameMode) {

        GameController gameController = new GameController();
    }

}