package it.polimi.ingsw.gameField;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IslandInfluenceTest {
    GameManager gameManager = GameManager.getInstance();
    Game game = gameManager.initGame("TwoPlayers",false);

    @Test
    void checkIslandInfluenceTest(){
        try {
            game.addPlayer("Io", DeckType.DRUID, TowerColor.BLACK);
            game.addPlayer("Tu",DeckType.SAGE,TowerColor.WHITE);

            Player p1 = game.getPlayersList().get(0);
            Player p2 = game.getPlayersList().get(1);

            //Adding two different color to board to simulate different influences
            Color colorToAddP1 = p1.getBoard().getEntryRoom().get(0);
            p1.getBoard().moveEntryToDiningRoom(colorToAddP1);
            game.checkInfluence(p1,colorToAddP1);

            //Make sure color of the second player is different form the first one
            int i = 0;
            Color colorToAddP2 = p2.getBoard().getEntryRoom().get(0);
            while(colorToAddP2.equals(colorToAddP1)){
                i++;
                colorToAddP2=p2.getBoard().getEntryRoom().get(i);
            }

            p2.getBoard().moveEntryToDiningRoom(colorToAddP2);
            game.checkInfluence(p2,colorToAddP2);

            //Simulating the adding of two student of that color on node 2 and 4
            for(int j=0;i<2;i++) {
                game.getGameField().getIslandNode(2).addStudent(colorToAddP1);
                game.getGameField().getIslandNode(4).addStudent(colorToAddP2);
            }

            game.checkIslandInfluence(2);
            game.checkIslandInfluence(4);


            assertEquals(game.getGameField().getIslandNode(2).getMostInfluencePlayer().getNickname(),p1.getNickname());
            assertEquals(game.getGameField().getIslandNode(2).getMostInfluencePlayer().getTowerColor(),p1.getTowerColor());

            assertEquals(game.getGameField().getIslandNode(4).getMostInfluencePlayer().getNickname(),p2.getNickname());
            assertEquals(game.getGameField().getIslandNode(4).getMostInfluencePlayer().getTowerColor(),p2.getTowerColor());

        } catch (FileNotFoundException e) {
            fail();
        } catch (NotEnoughSpace | NotOnBoardException | EndGameException e) {
            throw new RuntimeException(e);
        }


        game.getGameField();
    }
}
