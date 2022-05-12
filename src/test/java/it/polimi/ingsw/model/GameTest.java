package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameTest {

    @Test
    void getGameID() {

        Game game = GameManager.getInstance().initGame("TwoPlayers",false);

        assertNotNull(game.getGameID());
    }

    @Test
    void shouldRechargeCloud() {

        Game game2P = GameManager.getInstance().initGame("TwoPlayers",false);

        game2P.rechargeClouds();
    }



}