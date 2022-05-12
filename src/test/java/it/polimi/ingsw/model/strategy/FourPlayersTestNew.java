package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FourPlayersTestNew{

    @DisplayName("Testing FourPlayer strategy...")
    @Test
    void shouldGenerateFourPlayersGame() {

        FourPlayers fourPlayers = new FourPlayers();
        Game game = fourPlayers.generateGame(false);

        assertNotNull(game);

    }
}