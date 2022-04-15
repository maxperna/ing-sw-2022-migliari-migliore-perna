package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @DisplayName("Testing startGame method...")
    @ParameterizedTest
    @MethodSource ("gameModeList")
    void startGame(String gameMode) {

        try {
            GameManager.getInstance().startGame(gameMode);
            assertNotNull(GameManager.getInstance().getGamesList());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument " + "'"+ gameMode + "'");
        }

        for (int i = 0; i < GameManager.getInstance().getGamesList().size(); i++) {

            //ChecksGameField
            assertNotNull(GameManager.getInstance().getGame(i).getGameField());

            switch(GameManager.getInstance().getGame(i).getPlayersList().size()) {

                case 2: {

                    //Checks Players
                    assertEquals(TwoPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                    for(Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList())
                    {
                        assertEquals(TwoPlayers.maxStudentHall, currentPlayer.getBoard().getStudentsOutside().size());
                        assertEquals(TwoPlayers.maxTowers, currentPlayer.getBoard().getTowers().size());
                    }

                    //Checks CloudTile
                    assertEquals(TwoPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getGameField().getCloudsTile().size());

                    break;
                }

                case 4: {

                    //Checks Players
                    assertEquals(FourPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                    for(Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList())
                    {
                        assertEquals(FourPlayers.maxStudentHall, currentPlayer.getBoard().getStudentsOutside().size());
                        assertEquals(FourPlayers.maxTowers, currentPlayer.getBoard().getTowers().size());
                    }

                    //Checks CloudTile
                    assertEquals(FourPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getGameField().getCloudsTile().size());


                    break;
                }

                case 3: {

                    //Checks Players
                    assertEquals(ThreePlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                    for(Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList())
                    {
                        assertEquals(ThreePlayers.maxStudentHall, currentPlayer.getBoard().getStudentsOutside().size());
                        assertEquals(ThreePlayers.maxTowers, currentPlayer.getBoard().getTowers().size());
                    }

                    //Checks CloudTile
                    assertEquals(ThreePlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getGameField().getCloudsTile().size());

                    break;
                }

            }

        }

    }

    @DisplayName("Testing getInstance...")
    @Test
    void getInstance() {

        assertNotNull(GameManager.getInstance());
    }

    @DisplayName("Testing getGameList...")
    @Test
    void getGamesList() {

        assertNotNull(GameManager.getInstance().getGamesList());
    }

    @ParameterizedTest
    @CsvSource({"TwoPlayers,100", "ThreePlayers,0", "FourPlayers,1"})
    void getGame(String gameMode, int index) {

        try {
            Game game = GameManager.getInstance().getGame(index);
            assertNotNull(game);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }

        GameManager.getInstance().startGame(gameMode);
    }

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers", "...");
    }


}