package it.polimi.ingsw.model;

import it.polimi.ingsw.model.factory.TwoPlayers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @ParameterizedTest
    @MethodSource("gameModeList")
    void shouldCreateGame(String gameModeList) {

        Game game = new Game();
        game.createGame(gameModeList);
        int numOfPlayers = 0;

        switch (gameModeList) {
            case "TwoPlayers":
                numOfPlayers = 2;
                break;

            case "ThreePlayers":
                numOfPlayers = 3;
                break;

            case "FourPlayers":
                numOfPlayers = 4;
                break;
        }
        assertNotEquals(0, numOfPlayers);

        //Check gameID
        assertNotNull(game.getGameID());

        //Check gameFactory
        assertNotNull(game.getGameFactory());

        //Check players
        assertEquals(numOfPlayers, game.getPlayersList().size());
        //Check boards
        for (Player currentPlayer : game.getPlayersList()) {
            assertNotNull(currentPlayer.getBoard());
            assertNotNull(currentPlayer.getBoard().getTowers());
            assertNotNull(currentPlayer.getBoard().getStudentsOutside());
            assertNotNull(currentPlayer.getBoard().getBoardID());
        }

        //Check gameField
        assertNotNull(game.getGameField());
        //Check islandList
        assertNotNull(game.getGameField().getIslands());
        //Check pouch
        assertNotNull(game.getGameField().getPouch());
        //Check CloudsTile
        assertEquals(numOfPlayers, game.getGameField().getCloudsTile().size());

//        //Prints results
//        System.out.println("numero di giocatori " + game.getPlayersList().size());
//        for (Player player : game.getPlayersList()) {
//            System.out.println("-------- Player ----------------------------------------------------------------------------");
//            for (int i = 0; i < player.getBoard().getStudentsOutside().size(); i++)
//                System.out.println(player.getBoard().getStudentsOutside().get(i));
//            for (int k = 0; k < player.getBoard().getTowers().size(); k++)
//                System.out.println(player.getBoard().getTowers().get(k));
//        }

    }

    private static List<String> gameModeList() {
        return Arrays.asList("TwoPlayers", "ThreePlayers", "FourPlayers");
    }
}




