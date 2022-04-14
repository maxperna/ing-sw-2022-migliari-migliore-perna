package it.polimi.ingsw.model.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

            switch(GameManager.getInstance().getGamesList().get(i).getPlayersList().size()) {

                case 2: {
                    assertEquals(TwoPlayers.numberOfPlayers, GameManager.getInstance().getGamesList().get(i).getPlayersList().size());
                    break;
                }

                case 4: {
                    assertEquals(FourPlayers.numberOfPlayers, GameManager.getInstance().getGamesList().get(i).getPlayersList().size());
                    break;
                }

                case 3: {
                    assertEquals(ThreePlayers.numberOfPlayers, GameManager.getInstance().getGamesList().get(i).getPlayersList().size());
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

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers", "...");
    }
}