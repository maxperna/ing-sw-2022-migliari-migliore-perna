package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TwoPlayersTest {

    @DisplayName("Testing TwoPlayers strategy...")
    @Test
    void ShouldCreateTwoPlayersGame() {

        TwoPlayers twoPlayers = new TwoPlayers();
        Game game = twoPlayers.generateGame(false);

        assertNotNull(game);

    }
}