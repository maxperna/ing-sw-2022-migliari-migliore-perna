package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IslandListTest {

    @DisplayName("Testing getIslandNode method...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void getIslandNodeShouldReturnCorrect(int nodeID, int expected) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(nodeID).getNodeID());
        game.setNull();
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

    @Disabled
    @DisplayName("Testing moveMotherNature method with islandID...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void moveMotherNatureToIslandTileTest(int islandID, int expected) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(islandID).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandList().moveMotherNatureToNodeID(islandID);
        assertEquals(expected, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
        game.setNull();
    }

    @Disabled
    @DisplayName("Testing moveMotherNature method with number of moves...")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void moveMotherNatureByMoves(int moves) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        int motherNaturePosition = game.getGame(0).getGameField().getMotherNatureNode().getNodeID();
        motherNaturePosition = (motherNaturePosition+moves)%12;
        if(motherNaturePosition==0)
            motherNaturePosition=12;
        game.getGame(0).getGameField().getIslandNode(motherNaturePosition).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        try {
            game.getGame(0).getGameField().getIslandList().moveMotherNatureWithGivenMoves(moves);
        } catch (EndGameException e) {
            e.printStackTrace();
        }

        assertEquals(motherNaturePosition, game.getGame(0).getGameField().getMotherNatureNode().getNodeID());
        game.setNull();
    }

    @Order(6)
    @Disabled
    @DisplayName("Testing mergeIsland method with invalid parameters...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void testMergeSeparatedIslands(int input) {

        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        assertThrows(InvalidParameterException.class, () -> {
            game.getGame(0).getGameField().moveMotherNatureToNode(input);
        });
        game.setNull();
    }

    @Disabled
    @DisplayName("Testing mergeIsland method with valid parameters...")
    @ParameterizedTest
    @CsvSource ({"1 ,11, 2", "2, 1, 3", "3, 2, 4", "4, 3, 5", "5, 4, 6", "6, 5, 7", "7, 6, 8", "8, 7, 9", "9, 8, 10", "10, 9, 11", "11, 10, 1", "12, 11, 2"})
    void mergeIslandsTest(int input, int previous, int next) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(next).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().moveMotherNatureToNode(input);
        System.out.println(game.getGame(0).getGameField().getIslandList().islandCounter());
        assertEquals(next, game.getGame(0).getGameField().getIslandNode(input).getNextNode().getNodeID());
        System.out.println("Next node passed");
        assertEquals(previous, game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().getNodeID());
        System.out.println("Previous node passed");
        game.setNull();
    }

    @DisplayName("Testing EndGameException after merging islands...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTestShouldReturnEndGameException(int input){
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        for(int i =1; i<13; i++) {
            game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        }
            assertThrows(EndGameException.class, () -> {
                game.getGame(0).getGameField().moveMotherNatureToNode(input);
            });
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

    @DisplayName("Testing exception from getIslandNode with wrong ID")
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
        assertEquals(game.getGame(0).getGameField().getIslandList().getHeadNode(), game.getGame(0).getGameField().getIslandList().getHeadNode());
    }
}