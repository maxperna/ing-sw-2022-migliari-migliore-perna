package it.polimi.ingsw.model;

import it.polimi.ingsw.circularLinkedList.IslandList;
import it.polimi.ingsw.circularLinkedList.Node;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameTest {

    @Test
    void getGameID() {

        Game game = new Game(2, 1, 1);



        assertNotNull(game.getGameID());
    }

}