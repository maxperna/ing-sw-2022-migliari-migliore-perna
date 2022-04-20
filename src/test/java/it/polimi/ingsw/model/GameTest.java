package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameTest {

    @Test
    void getGameID() {

        Game game = new Game(2, 1, 1);



        assertNotNull(game.getGameID());
    }

}