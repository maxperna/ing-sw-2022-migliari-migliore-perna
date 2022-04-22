package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.model.GameManager;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class IslandListTest {

    /**
     * test, returns the order of islandTIles, if only this method is started, it shows all 12 islands, if all class test is started, it shows the list after the merge
     */
    @Test
    void testIsoleInOrdine() {
    GameManager game = GameManager.getInstance();
    game.startGame("TwoPlayers");
    IslandList islandList= game.getGame(0).getGameField().getIslandList();
    for (int i=0; i<12; i++) {
        System.out.println("L'isola con ID " +islandList.getArrayListOfIslandTile(i+1).get(0).getID()+ " si trova nel nodo " +islandList.getIslandNode(i+1).ID);
        System.out.println("Nel nodo precedente si trova l'isola con ID " +islandList.getIslandNode(i+1).getPreviousNode().getIslandTiles().get(0).getID());
        System.out.println("Nel nodo successivo si trova l'isola con ID " +islandList.getIslandNode(i+1).getNextNode().getIslandTiles().get(0).getID());
    }
}

    /**
     * test that shows what happens when we call merge on two next islands
     */
    @Test
    void testMergeIslands() {

        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().mergeIsland(1,2);
        assertEquals(game.getGame(0).getGameField().getIslandNode(1), game.getGame(0).getGameField().getIslandNode(2));
        System.out.println("Le isole 1 e 2 sono nello stesso nodo");
        assertEquals(game.getGame(0).getGameField().getIslandList().getIslandNode(1).getNextNode(), game.getGame(0).getGameField().getIslandList().getIslandNode(2).getNextNode());
        System.out.println("Il nodo successivo è uguale");


        assertNotNull(game.getGame(0).getGameID());
    }

    /**
     * test that shows what happens when we call merge on two islands that are not next to each other
     */
    @Test
    void testMergeSeparatedIslands() {

        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            game.getGame(0).getGameField().mergeIsland(1,4);
        });
        System.out.println("Exception lanciata");

    }


}