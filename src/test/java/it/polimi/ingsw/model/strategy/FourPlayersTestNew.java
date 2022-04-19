package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.strategy.FourPlayers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FourPlayersTestNew {

    @DisplayName("Testing FourPlayer strategy...")
    @Test
    void shouldGenerateFourPlayersGame() {

        FourPlayers fourPlayers = new FourPlayers();
        Game game = fourPlayers.generateGame();

        assertNotNull(game);

        //Check players
        assertEquals(numberOfPlayers, game.getPlayersList().size());

        //Check board
        for (int i = 0; i < numberOfPlayers; i++) {
            assertEquals(maxStudentHall, game.getPlayersList().get(i).getBoard().getStudentsOutside().size());
            assertEquals(maxTowers, game.getPlayersList().get(i).getBoard().getTowers().size());
        }

        //Check teamMate
        assertEquals(game.getPlayersList().get(0).getBoard().getTowers(), game.getPlayersList().get(3).getBoard().getTowers());
        assertEquals(game.getPlayersList().get(1).getBoard().getTowers(), game.getPlayersList().get(2).getBoard().getTowers());
    }
}