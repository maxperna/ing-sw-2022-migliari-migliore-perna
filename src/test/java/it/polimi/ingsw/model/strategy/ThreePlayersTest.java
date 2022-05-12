package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ThreePlayersTest {

    @DisplayName("Testing ThreePlayers strategy...")
    @Test
    void ShouldGenerateThreePlayersGame() {
        ThreePlayers threePlayers = new ThreePlayers();
        Game game = threePlayers.generateGame(false);

        assertNotNull(game);

    }
}