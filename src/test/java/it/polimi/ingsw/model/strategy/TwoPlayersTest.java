package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.strategy.TwoPlayers.*;
import static org.junit.jupiter.api.Assertions.*;

class TwoPlayersTest {

    @DisplayName("Testing TwoPlayers strategy...")
    @Test
    void ShouldCreateTwoPlayersGame() {

        TwoPlayers twoPlayers = new TwoPlayers();
        Game game = twoPlayers.generateGame();

        assertNotNull(game);

        //Check players
        assertEquals(numberOfPlayers, game.getPlayersList().size());

        //Check board
        for(int i = 0; i < numberOfPlayers; i++) {
            assertEquals(maxStudentHall, game.getPlayersList().get(i).getBoard().getStudentsOutside().size());
            assertEquals(maxTowers, game.getPlayersList().get(i).getBoard().getTowers().size());
        }

    }
}