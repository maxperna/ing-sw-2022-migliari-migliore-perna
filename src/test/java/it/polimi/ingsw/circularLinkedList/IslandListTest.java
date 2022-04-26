package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class IslandListTest {

    @DisplayName("Testing getIslandNode method...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void getIslandNodeShouldReturnCorrect(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(islandID).getNodeID());
        game.setNull();
    }

    @DisplayName("Testing getNextNode method...")
    @ParameterizedTest
    @CsvSource({"1,2", "2, 3", "3, 4", "4, 5", "5, 6", "6,7", "7,8", "8,9", "9,10", "10,11", "11,12", "12,1"})
    void getNextIslandNodeShouldReturnCorrect(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(islandID).getNextNode().getNodeID());
        game.setNull();
    }


    @DisplayName("Testing getPreviousNode method...")
    @ParameterizedTest
    @CsvSource({"1,12", "2,1", "3,2", "4,3", "5,4", "6,5", "7,6", "8,7", "9,8", "10,9", "11,10", "12,11"})
    void getPreviousIslandNodeShouldReturnCorrect() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        for(int i=1; i<=12; i++) {
            if (i == 1)
                assertEquals(12, game.getGame(0).getGameField().getIslandNode(i).getPreviousNode().getNodeID());
            else
                assertEquals(i-1, game.getGame(0).getGameField().getIslandNode(i).getPreviousNode().getNodeID());
        }
        game.setNull();
    }

    @DisplayName("Testing moveMotherNature method with islandID...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void moveMotherNatureToIslandTileTest(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandList().moveMotherNatureToIslandTile(islandID);
        assertEquals(expected, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
        game.setNull();
    }

    @DisplayName("Testing moveMotherNature method with number of moves...")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void moveMotherNatureByMoves(int moves) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        int motherNaturePosition = game.getGame(0).getGameField().getMotherNatureNode().getNodeID();
        System.out.println(motherNaturePosition);
        game.getGame(0).getGameField().getIslandList().moveMotherNatureWithGivenMoves(moves);
        motherNaturePosition = (motherNaturePosition+moves)%12;
        if(motherNaturePosition==0)
            motherNaturePosition=12;
        assertEquals(motherNaturePosition, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
    }

    @Order(6)
    @DisplayName("Testing mergeIsland method with invalid parameters...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void testMergeSeparatedIslands(int input) {

        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertThrows(InvalidParameterException.class, () -> {
            game.getGame(0).getGameField().mergeIsland(input,(input+2)%12);
        });
        game.setNull();
    }

    @DisplayName("Testing mergeIsland method with valid parameters...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTest(int input) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().moveMotherNatureToAGivenIsland(input);
        int next = ((input+1)%12);
        if(input==11)
            next=12;
        try {
            game.getGame(0).getGameField().mergeIsland(input, next);
        } catch (EndGameException e) {
            e.printStackTrace();
        }
        assertEquals(game.getGame(0).getGameField().getIslandNode(input), game.getGame(0).getGameField().getIslandNode(next));
            assertEquals(game.getGame(0).getGameField().getIslandList().getIslandNode(input).getNextNode(), game.getGame(0).getGameField().getIslandList().getIslandNode(next).getNextNode());
            assertEquals(game.getGame(0).getGameField().getIslandList().getIslandNode(input).getPreviousNode(), game.getGame(0).getGameField().getIslandList().getIslandNode(next).getPreviousNode());
        game.setNull();
    }

    @DisplayName("Testing EndGameException after merging islands...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTestShouldReturnEndGameException(int input) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        int next = ((input+1)%12);
        if(input==11)
            next=12;
        game.getGame(0).getGameField().moveMotherNatureToAGivenIsland(input);
        while(game.getGame(0).getGameField().getIslandList().islandCounter()>4) {
            game.getGame(0).getGameField().mergeIsland(input, next);
            next++;
            if(next>12)
                next=next-12;
        }
        int finalNext = next;
        assertThrows(EndGameException.class, () -> game.getGame(0).getGameField().mergeIsland(input, finalNext));
        game.setNull();
    }

    @DisplayName("Adding students with addStudents method, using getMostInfluence to check correct result")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void addThreeStudents (int ID) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandList().addStudent(ID, Color.RED);
        game.getGame(0).getGameField().getIslandList().addStudent(ID, Color.RED);
        game.getGame(0).getGameField().getIslandList().addStudent(ID, Color.RED);
        assertEquals(Color.RED, game.getGame(0).getGameField().getIslandList().getMostInfluence(game.getGame(0).getGameField().getIslandNode(ID)));
    }

    @DisplayName("Testing exception from getIslandNode with worng ID")
    @Test
    void getIslandNodeException () {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertThrows(InvalidParameterException.class, () -> game.getGame(0).getGameField().getIslandList().getIslandNode(15));
    }

    @DisplayName("Testing getHead method")
    @Test
    void getHead () {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(game.getGame(0).getGameField().getIslandList().head, game.getGame(0).getGameField().getIslandList().getHeadNode());
    }
}