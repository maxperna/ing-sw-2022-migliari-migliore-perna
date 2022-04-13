package it.polimi.ingsw.model.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @ParameterizedTest
    @MethodSource ("gameModeList")
    void startGame(String gameMode) {

        try {
            GameManager.getInstance().startGame(gameMode);
            assertNotNull(GameManager.getInstance().getGamesList());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument " + "'"+ gameMode + "'");
        }

    }

    @Test
    void getInstance() {

        assertNotNull(GameManager.getInstance());
    }

    @Test
    void getGamesList() {

        assertNotNull(GameManager.getInstance().getGamesList());
    }

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers", "...");
    }
}