package it.polimi.ingsw.gameField;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class IslandListTest {

    @DisplayName("Testing getIslandNode method...")
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3", "4,4", "5,5", "6,6", "7,7", "8,8", "9,9", "10,10", "11,11", "12,12"})
    void getIslandNodeShouldReturnCorrect(int nodeID, int expected) {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(nodeID).getNodeID());
    }

    @DisplayName("Testing getNextNode method...")
    @ParameterizedTest
    @CsvSource({"1,2", "2, 3", "3, 4", "4, 5", "5, 6", "6,7", "7,8", "8,9", "9,10", "10,11", "11,12", "12,1"})
    void getNextIslandNodeShouldReturnCorrect(int islandID, int expected) {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
        assertEquals(expected, game.getGame(0).getGameField().getIslandNode(islandID).getNextNode().getNodeID());
    }


    @DisplayName("Testing getPreviousNode method...")
    @ParameterizedTest
    @CsvSource({"1,12", "2,1", "3,2", "4,3", "5,4", "6,5", "7,6", "8,7", "9,8", "10,9", "11,10", "12,11"})
    void getPreviousIslandNodeShouldReturnCorrect() {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
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

        try {
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            game.getGameField().getIslandNode(islandID).setMostInfluencePlayer(game.getPlayersList().get(0));
            game.getGameField().moveMotherNatureToNodeID(islandID);
            assertEquals(expected, game.getGameField().getMotherNature().getNodeID());
            GameManager.getInstance().setNull();

        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @DisplayName("Testing moveMotherNature method with number of moves...")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void moveMotherNatureByMoves(int moves) {
        try {
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            int motherNaturePosition = game.getGameField().getMotherNature().getNodeID();
            motherNaturePosition = (motherNaturePosition+moves)%12;
            if(motherNaturePosition==0)
                motherNaturePosition=12;
            game.getGameField().getIslandNode(motherNaturePosition).setMostInfluencePlayer(game.getPlayersList().get(0));

            game.getGameField().moveMotherNatureWithGivenMoves(moves);

            assertEquals(motherNaturePosition, game.getGameField().getMotherNature().getNodeID());
            GameManager.getInstance().setNull();
        } catch (EndGameException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            fail();
        }


    }

    @DisplayName("Testing mergeIsland method with a node and its next one...")
    @ParameterizedTest
    @CsvSource ({"1 ,11, 2", "2, 1, 3", "3, 2, 4", "4, 3, 5", "5, 4, 6", "6, 5, 7", "7, 6, 8", "8, 7, 9", "9, 8, 10", "10, 9, 11", "11, 10, 1", "12, 11, 2"})
    void mergeIslandsOnNextNodeTest(int input, int previous, int next) {
        try {
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            game.getGameField().getIslandNode(input).setMostInfluencePlayer(game.getPlayersList().get(1));
            game.getGameField().getIslandNode(input).setTowerTest(TowerColor.BLACK);
            game.getGameField().getIslandNode(input).getNextNode().setMostInfluencePlayer(game.getPlayersList().get(1));
            game.getGameField().getIslandNode(input).getNextNode().setTowerTest(TowerColor.BLACK);
            game.getGameField().moveMotherNatureToNodeID(input);
            if(input == 12)
                input=1;
            assertEquals(next, game.getGameField().getIslandNode(input).getNextNode().getNodeID());
            assertEquals(previous, game.getGameField().getIslandNode(input).getPreviousNode().getNodeID());
            GameManager.getInstance().setNull();
        } catch (EndGameException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @DisplayName("Testing mergeIsland method with a node and its previous one...")
    @ParameterizedTest
    @CsvSource ({"1 ,11, 2", "2, 1, 3", "3, 2, 4", "4, 3, 5", "5, 4, 6", "6, 5, 7", "7, 6, 8", "8, 7, 9", "9, 8, 10", "10, 9, 11", "11, 10, 1", "12, 10, 1"})
    void mergeIslandsOnPreviousNodeTest(int input, int previous, int next) throws EndGameException {
        try {
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            game.getGameField().getIslandNode(input).setMostInfluencePlayer(game.getPlayersList().get(1));
            game.getGameField().getIslandNode(input).setTowerTest(TowerColor.BLACK);
            game.getGameField().getIslandNode(input).getPreviousNode().setMostInfluencePlayer(game.getPlayersList().get(1));
            game.getGameField().getIslandNode(input).getPreviousNode().setTowerTest(TowerColor.BLACK);
            game.getGameField().moveMotherNatureToNodeID(input);
            if(input == 12)
                input--;
            assertEquals(next, game.getGameField().getIslandNode(input).getNextNode().getNodeID());
            assertEquals(previous, game.getGameField().getIslandNode(input).getPreviousNode().getNodeID());
            GameManager.getInstance().setNull();

        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @DisplayName("Testing EndGameException after merging islands...")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void mergeIslandsTestShouldReturnEndGameException(int input){
        try {
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            for(int i =1; i<13; i++) {
                game.getGameField().getIslandNode(i).setMostInfluencePlayer(game.getPlayersList().get(1));              //setting the mostInfluencePlayer as the player with the BLACK towers
            }

            for(int i=1; i<13; i++) {
                game.getGameField().getIslandNode(i).setTowerTest(TowerColor.BLACK);
            }

            System.out.println(game.getPlayersList().get(0).getNickname());
                assertThrows(EndGameException.class, () -> {
                    game.getGameField().moveMotherNatureToNodeID(input);
                });
            GameManager.getInstance().setNull();

        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @DisplayName("Adding students with addStudents method, using getMostInfluence to check correct result")
    @ParameterizedTest
    @CsvSource ({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    void addThreeStudents (int ID) {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
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
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            game.getGameField().getIslandNode(input).setMostInfluencePlayer(game.getPlayersList().get(0));
            game.getGameField().getIslandNode(input).setTower();
            game.getGameField().getIslandNode(input).getNextNode().setMostInfluencePlayer(game.getPlayersList().get(0));
            game.getGameField().getIslandNode(input).getNextNode().setTower();
            game.getGameField().getIslandNode(input).getPreviousNode().setMostInfluencePlayer(game.getPlayersList().get(0));
            game.getGameField().getIslandNode(input).getPreviousNode().setTower();
            game.getGameField().moveMotherNatureToNodeID(input);
            if(input == 1) {
                assertEquals(next, game.getGameField().getIslandNode(input).getNextNode().getNodeID());
                assertEquals(previous, game.getGameField().getIslandNode(input).getPreviousNode().getNodeID());
            }
            else if(input==12){
                assertEquals(next, game.getGameField().getIslandNode(1).getNextNode().getNodeID());
                assertEquals(previous, game.getGameField().getIslandNode(1).getPreviousNode().getNodeID());
            }
            else {
                assertEquals(next, game.getGameField().getIslandNode(input-1).getNextNode().getNodeID());
                assertEquals(previous, game.getGameField().getIslandNode(input-1).getPreviousNode().getNodeID());
            }
            GameManager.getInstance().setNull();
        } catch (FileNotFoundException e) {
            fail();
        }
    }
}