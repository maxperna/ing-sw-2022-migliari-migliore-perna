package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.model.GameManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class IslandListTest {

    @DisplayName("Testing getIslandNode method...")
    @ParameterizedTest
    @CsvSource({"1, 1", "2, 2", "3, 3", "4, 4", "5, 5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void getIslandNodeShouldReturnCorrect(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(islandID).getNodeID());
    }

    @DisplayName("Testing getNextNode method...")
    @ParameterizedTest
    @CsvSource({"1,2", "2, 3", "3, 4", "4, 5", "5, 6", "6,7", "7,8", "8,9", "9,10", "10,11", "11,12", "12,1"})
    void getNextIslandNodeShouldReturnCorrect(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(islandID).getNextNode().getNodeID());
    }


    @DisplayName("Testing getPreviousNode method...")
    @Test
    void getPreviousIslandNodeShouldReturnCorrect() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        for(int i=1; i<=12; i++) {
            if (i == 1)
                assertEquals(12, game.getGame(0).getGameField().getIslandNode(i).getPreviousNode().getNodeID());
            else
                assertEquals(i-1, game.getGame(0).getGameField().getIslandNode(i).getPreviousNode().getNodeID());
        }
    }

    @DisplayName("Testing moveMotherNature method with islandID...")
    @ParameterizedTest
    @CsvSource({"1, 1", "2, 2", "3, 3", "4, 4", "5, 5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void moveMotherNatureToIslandTileTest(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandList().moveMotherNatureToIslandTile(islandID);
        assertEquals(expected, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
    }

    @DisplayName("Testing moveMotherNature method with number of moves...")
    @ParameterizedTest
    @CsvSource({"1","2","3","4","5"})
    void moveMotherNatureByMoves(int moves) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        int motherNaturePosition = game.getGame(0).getGameField().getMotherNatureNode().getNodeID();
        game.getGame(0).getGameField().getIslandList().moveMotherNatureWithGivenMoves(moves);
        if(motherNaturePosition+moves>12) {
            motherNaturePosition = motherNaturePosition+moves-12;
        }
        else
            motherNaturePosition = motherNaturePosition+moves;
        assertEquals(motherNaturePosition, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
    }

    @DisplayName("Testing mergeIsland method with valid parameters...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTest(int input) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().moveMotherNatureToAGivenIsland(input);
        int next;
        if(input==12)
            next=1;
        else
            next=input+1;
        game.getGame(0).getGameField().mergeIsland(input,next);
        assertEquals(game.getGame(0).getGameField().getIslandNode(input), game.getGame(0).getGameField().getIslandNode(next));
        assertEquals(game.getGame(0).getGameField().getIslandList().getIslandNode(input).getNextNode(), game.getGame(0).getGameField().getIslandList().getIslandNode(next).getNextNode());
        assertEquals(game.getGame(0).getGameField().getIslandList().getIslandNode(input).getPreviousNode(), game.getGame(0).getGameField().getIslandList().getIslandNode(next).getPreviousNode());
    }

    @DisplayName("Testing mergeIsland method with invalid parameters...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void testMergeSeparatedIslands(int input) {

        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            game.getGame(0).getGameField().mergeIsland(input,(input+2)%12);
        });
    }
}