package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.TowerColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


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

    @DisplayName("Testing moveMotherNature method with islandID...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void moveMotherNatureToIslandTileTest(int islandID, int expected) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(islandID).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().moveMotherNatureToNodeID(islandID);
        assertEquals(expected, game.getGame(0).getGameField().getMotherNature().getNodeID());
        game.setNull();
    }

    @DisplayName("Testing moveMotherNature method with number of moves...")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void moveMotherNatureByMoves(int moves) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        int motherNaturePosition = game.getGame(0).getGameField().getMotherNature().getNodeID();
        motherNaturePosition = (motherNaturePosition+moves)%12;
        if(motherNaturePosition==0)
            motherNaturePosition=12;
        game.getGame(0).getGameField().getIslandNode(motherNaturePosition).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        try {
            game.getGame(0).getGameField().moveMotherNatureWithGivenMoves(moves);
        } catch (EndGameException e) {
            e.printStackTrace();
        }

        assertEquals(motherNaturePosition, game.getGame(0).getGameField().getMotherNature().getNodeID());
        game.setNull();
    }

    @DisplayName("Testing mergeIsland method with a node and its next one...")
    @ParameterizedTest
    @CsvSource ({"1 ,11, 2", "2, 1, 3", "3, 2, 4", "4, 3, 5", "5, 4, 6", "6, 5, 7", "7, 6, 8", "8, 7, 9", "9, 8, 10", "10, 9, 11", "11, 10, 1", "12, 11, 2"})
    void mergeIslandsOnNextNodeTest(int input, int previous, int next) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().getIslandNode(input).getNextNode().setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).getNextNode().setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().moveMotherNatureToNodeID(input);
        if(input == 12)
            input=1;
        assertEquals(next, game.getGame(0).getGameField().getIslandNode(input).getNextNode().getNodeID());
        assertEquals(previous, game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().getNodeID());
        game.setNull();
    }

    @DisplayName("Testing mergeIsland method with a node and its previous one...")
    @ParameterizedTest
    @CsvSource ({"1 ,11, 2", "2, 1, 3", "3, 2, 4", "4, 3, 5", "5, 4, 6", "6, 5, 7", "7, 6, 8", "8, 7, 9", "9, 8, 10", "10, 9, 11", "11, 10, 1", "12, 10, 1"})
    void mergeIslandsOnPreviousNodeTest(int input, int previous, int next) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().moveMotherNatureToNodeID(input);
        if(input == 12)
            input--;
        assertEquals(next, game.getGame(0).getGameField().getIslandNode(input).getNextNode().getNodeID());
        assertEquals(previous, game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().getNodeID());
        game.setNull();
    }

    @DisplayName("Testing EndGameException after merging islands...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTestShouldReturnEndGameException(int input){
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        for(int i=1; i<13; i++) {
            game.getGame(0).getGameField().getIslandNode(i).setTowerTest(TowerColor.BLACK);
        }
        for(int i =1; i<13; i++) {
            game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        }
            assertThrows(EndGameException.class, () -> {
                game.getGame(0).getGameField().moveMotherNatureToNodeID(input);
            });
        game.setNull();
    }

    @DisplayName("Adding students with addStudents method, using getMostInfluence to check correct result")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void addThreeStudents (int ID) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().addStudent(ID, Color.RED);
        game.getGame(0).getGameField().addStudent(ID, Color.RED);
        game.getGame(0).getGameField().addStudent(ID, Color.RED);
        if(game.getGame(0).getGameField().getIslandNode(ID).getColorInfluence(Color.RED) == 4)
            assertEquals(4, game.getGame(0).getGameField().getIslandNode(ID).getColorInfluence(Color.RED));
        else
            assertEquals(3, game.getGame(0).getGameField().getIslandNode(ID).getColorInfluence(Color.RED));
        game.setNull();
    }

    @DisplayName("Testing mergeIsland method with a node, its next one and its previous one...")
    @ParameterizedTest
    @CsvSource ({"1 ,10, 2", "2, 10, 2", "3, 1, 3", "4, 2, 4", "5, 3, 5", "6, 4, 6", "7, 5, 7", "8, 6, 8", "9, 7, 9", "10, 8, 10", "11, 9, 1", "12, 10, 2"})
    void mergeIslandsOnNextAndPreviousNodeTest(int input, int previous, int next) throws EndGameException {
        GameManager game = GameManager.getInstance();
        game.setNull();
        game = GameManager.getInstance();
        game.startGame("TwoPlayers");
        game.getGame(0).getGameField().getIslandNode(input).setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().getIslandNode(input).getNextNode().setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).getNextNode().setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().setMostInfluencePlayer(game.getGame(0).getPlayersList().get(0));
        game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().setTowerTest(TowerColor.BLACK);
        game.getGame(0).getGameField().moveMotherNatureToNodeID(input);
        if(input == 1) {
            assertEquals(next, game.getGame(0).getGameField().getIslandNode(input).getNextNode().getNodeID());
            assertEquals(previous, game.getGame(0).getGameField().getIslandNode(input).getPreviousNode().getNodeID());
        }
        else if(input==12){
            assertEquals(next, game.getGame(0).getGameField().getIslandNode(1).getNextNode().getNodeID());
            assertEquals(previous, game.getGame(0).getGameField().getIslandNode(1).getPreviousNode().getNodeID());
        }
        else {
            assertEquals(next, game.getGame(0).getGameField().getIslandNode(input-1).getNextNode().getNodeID());
            assertEquals(previous, game.getGame(0).getGameField().getIslandNode(input-1).getPreviousNode().getNodeID());
        }
        game.setNull();
    }
}