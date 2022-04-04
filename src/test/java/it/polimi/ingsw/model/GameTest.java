package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

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

        //Check gameID
        Assertions.assertNotNull(game.getGameID());

        //Check gameFactory
        Assertions.assertNotNull(game.getGameFactory());

        //Check players
        Assertions.assertEquals(numOfPlayers, game.getPlayersList().size());

        //Check gameField

        //Prints results
        System.out.println("numero di giocatori " + game.getPlayersList().size());
        for (Player player : game.getPlayersList()) {
            System.out.println("-------- Player ----------------------------------------------------------------------------");
            for (int i = 0; i < player.getBoard().getStudentsOutside().size(); i++)
                System.out.println(player.getBoard().getStudentsOutside().get(i));
            for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                System.out.println(player.getBoard().getTowers().get(k));
        }

    }

    private static List<String> gameModeList() {
        return Arrays.asList("TwoPlayers", "ThreePlayers", "FourPlayers");
    }
}




