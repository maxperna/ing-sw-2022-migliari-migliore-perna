package it.polimi.ingsw.gameField;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.TowerColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class NodeTest {

    @DisplayName("Testing initial stopIsland parameter is false...")
    @Test
    void isStopped() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        for(int index = 1; index <13; index++) {
            assertEquals(false, game.getGame(0).getGameField().getIslandNode(index).isStopped());
        }
    }

    @DisplayName("Testing setStop method...")
    @Test
    void stopIsland() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        for(int index = 1; index <13; index++) {
            game.getGame(0).getGameField().getIslandNode(index).stopIsland();
            assertEquals(true, game.getGame(0).getGameField().getIslandNode(index).isStopped());
        }
        game.setNull();
    }

    @DisplayName("Testing setPreviousNode method...")
    @ParameterizedTest
    @CsvSource({"1,12", "2,1", "3,2", "4,3", "5,4", "6,5 ", "7,6", "8,7", "9,8", "10,9", "11,10", "12,11"})
    void setPreviousNode(int ID, int previous) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        game.getGame(0).getGameField().getIslandNode(ID).setPreviousNode(game.getGame(0).getGameField().getIslandNode(ID).getPreviousNode());
        assertEquals(previous, game.getGame(0).getGameField().getIslandNode(ID).getPreviousNode().getNodeID());
    }

    @DisplayName("Testing setNextNode method...")
    @ParameterizedTest
    @CsvSource({"1,2", "2, 3", "3, 4", "4, 5", "5, 6", "6,7", "7,8", "8,9", "9,10", "10,11", "11,12", "12,1"})
    void setNextNode(int ID, int next) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        game.getGame(0).getGameField().getIslandNode(ID).setNextNode(game.getGame(0).getGameField().getIslandNode(ID).getNextNode());
        assertEquals(next, game.getGame(0).getGameField().getIslandNode(ID).getNextNode().getNodeID());
    }


    @DisplayName("Testing setMotherNature method...")
    @Test
    void setMotherNature() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        for(int ID = 1; ID<13; ID++) {
            game.getGame(0).getGameField().getIslandNode(ID).setMotherNature();
            assertEquals(true, game.getGame(0).getGameField().getIslandNode(ID).checkMotherNature());
        }
    }

    @DisplayName("Testing resetMotherNature method...")
    @Test
    void resetMotherNature() {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        for(int ID = 1; ID<13; ID++) {
            game.getGame(0).getGameField().getIslandNode(ID).resetMotherNature();
            assertEquals(false, game.getGame(0).getGameField().getIslandNode(ID).checkMotherNature());
        }
    }



    @DisplayName("Testing setTower and getTower method...")
    @Test
    void setTower() {
        try {
            Game game = GameManager.getInstance().startGame("TwoPlayers",false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            for(int ID = 1; ID<13; ID++) {
                game.getGameField().getIslandNode(ID).setMostInfluencePlayer(game.getPlayersList().get(0));
                game.getGameField().getIslandNode(ID).setTowerTest(TowerColor.BLACK);
                assertEquals(TowerColor.BLACK, game.getGameField().getIslandNode(ID).getTowerColor());
            }

        } catch (FileNotFoundException e) {
            fail();
        }
    }
}