package it.polimi.ingsw.experts;

import it.polimi.ingsw.controller.TurnLogic;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandList;
import it.polimi.ingsw.model.gameField.Node;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExpertsCardTest {

    GameManager GM = GameManager.getInstance();
    Game gameTest = GM.initGame("TwoPlayers",true);

    @Test
    @DisplayName("Expert1 test ")
    void expert1Test() throws FileNotFoundException {
        Expert1 exp1 = new Expert1(gameTest);
        ArrayList<Color> studentsOnCard = exp1.getStudentsOnCard();
        Node nodeTest = gameTest.getGameField().getIslandNode(2);
        assertEquals(studentsOnCard.size(),4);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 = gameTest.getPlayersList().get(0);

        gameTest.coinHandler(p1,5);

        try {
            Color colorTest = studentsOnCard.get(0);  //color pick to be tested
            exp1.useCard(p1,2,colorTest);
            Color colorAlreadyonIsl;
            try{
                colorAlreadyonIsl = nodeTest.getStudents().get(0);
            }catch (IndexOutOfBoundsException e){
                colorAlreadyonIsl = null;
            }
            //Check activation
            assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp1.getClass().getName());
            //Check effect
            if(colorAlreadyonIsl !=null && colorAlreadyonIsl.equals(colorTest)){
                //if is present another color on the island and it is the same
                assertEquals(2,nodeTest.getColorInfluence(colorTest));
            }
            else
                assertEquals(1,nodeTest.getColorInfluence(colorTest));

            //Check if another student is redrawn
            assertEquals(studentsOnCard.size(),4);
            //Check end of the effect
            exp1.endEffect();
            assertEquals(gameTest.getActiveExpertCard(),null);
        } catch (NotEnoughCoins | IllegalMove e) {
            fail();
        }
    }

    @Test
    @DisplayName("Expert2 test")
    void expert2Test() throws FileNotFoundException {
        Expert2 exp2 = new Expert2(gameTest);

        gameTest.addPlayer("tu",DeckType.DRUID,TowerColor.BLACK);
        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 = gameTest.getPlayersList().get(0);
        Player p2 = gameTest.getPlayersList().get(1);

        gameTest.coinHandler(p2,5);

        p1.getBoard().addToDiningTest(Color.RED);
        p1.getBoard().addToDiningTest(Color.BLUE);
        gameTest.checkInfluence(p1,Color.RED);
        gameTest.checkInfluence(p1,Color.BLUE);

        //Check if the configuration is correct

        assertEquals(p1.getBoard().getTeacher(Color.BLUE),true);
        assertEquals(p1.getBoard().getTeacher(Color.RED),true);

        p2.getBoard().addToDiningTest(Color.RED);
        p2.getBoard().addToDiningTest(Color.BLUE);

        gameTest.checkInfluence(p2,Color.RED);
        gameTest.checkInfluence(p2,Color.BLUE);

        assertEquals(p2.getBoard().getTeacher(Color.BLUE),false);
        assertEquals(p2.getBoard().getTeacher(Color.RED),false);

        try {
            exp2.useCard(p2);
            //Check the effective swap of professor
            assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp2.getClass().getName());
            assertEquals(p2.getBoard().getTeacher(Color.BLUE),true);
            assertEquals(p2.getBoard().getTeacher(Color.RED),true);

            assertEquals(p1.getBoard().getTeacher(Color.BLUE),false);
            assertEquals(p1.getBoard().getTeacher(Color.RED),false);

            exp2.endEffect();

            assertEquals(gameTest.getActiveExpertCard(),null);
            assertEquals(p2.getBoard().getTeacher(Color.BLUE),false);
            assertEquals(p2.getBoard().getTeacher(Color.RED),false);

            assertEquals(p1.getBoard().getTeacher(Color.BLUE),true);
            assertEquals(p1.getBoard().getTeacher(Color.RED),true);



        } catch (NotEnoughCoins e) {
            fail();
        }

    }

    @Test
    @DisplayName("Test expert 3")
    void expert3Test() throws FileNotFoundException {
        Expert3 exp3 = new Expert3(gameTest);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);

        Player p1 = gameTest.getPlayersList().get(0);

        gameTest.coinHandler(p1,5);

        Node testNode = gameTest.getGameField().getIslandNode(2);

        p1.getBoard().addToDiningTest(Color.BLUE);
        p1.getBoard().addToDiningTest(Color.BLUE);
        gameTest.checkInfluence(p1,Color.BLUE);

        testNode.addStudent(Color.BLUE);
        testNode.addStudent(Color.BLUE);

        try {
            exp3.useCard(p1,2);
            assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp3.getClass().getName());
            gameTest.checkIslandInfluence(2);
            assertEquals(testNode.getTowerColor(),p1.getTowerColor());
            assertEquals(testNode.getMostInfluencePlayer(),p1);


        } catch (NotEnoughCoins | EndGameException e) {
            fail();
        }

        exp3.endEffect();
        assertEquals(gameTest.getActiveExpertCard(),null);
    }

    @Test
    @DisplayName("Test expert 4")
    void expert4Test() throws FileNotFoundException {
        Expert4 exp4 = new Expert4(gameTest);

        TurnLogic turnLogicTest = new TurnLogic(gameTest);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 = gameTest.getPlayersList().get(0);

        gameTest.coinHandler(p1,5);

        try {
            turnLogicTest.setPlayedCard(3,p1);  //cart 3 maximum MN movement = 2
        } catch (CardAlreadyPlayed | EndGameException | InexistentCard e) {
            fail();
        }

        try {
            turnLogicTest.moveMotherNature(p1,4);
        } catch (IllegalMove e) {
            assert(true);   //right calling MN cannot move so much
        } catch (EndGameException e) {
            fail();
        }

        int actualNodeMN = gameTest.getGameField().getMotherNature().getNodeID();
        try {
            exp4.useCard(p1);
        } catch (NotEnoughCoins e) {
            fail();
        }
        try {
            turnLogicTest.moveMotherNature(p1,4);
        } catch (IllegalMove | EndGameException e) {
            fail();
        }

        assertEquals(actualNodeMN+4,gameTest.getGameField().getMotherNature().getNodeID());

        exp4.endEffect();
        assertEquals(gameTest.getActiveExpertCard(),null);
    }

    @Test
    @DisplayName("Test expert 5")

    void expert5Test() throws FileNotFoundException {
        Expert5 exp5 = new Expert5(gameTest);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 = gameTest.getPlayersList().get(0);

        //Mock experts card for inserting expert card 5
        ArrayList<ExpertCard> mockCards = new ArrayList<>();
        mockCards.add(exp5);
        mockCards.add(new Expert3(gameTest));
        mockCards.add(new Expert4(gameTest));

        gameTest.setExpertsCardTest(mockCards);

        gameTest.coinHandler(p1,5);

        Node nodeTest = gameTest.getGameField().getIslandNode(2);

        //Check island is not stopped
        assertEquals(nodeTest.isStopped(),false);

        try {
            exp5.useCard(p1,2);
            assertEquals(exp5.getStopAvailable(),3);
            assertEquals(nodeTest.isStopped(),true);
        } catch (NotEnoughCoins | IllegalMove e) {
            fail();
        }

        //Test if is stopped works
        p1.getBoard().addToDiningTest(Color.BLUE);
        gameTest.checkInfluence(p1,Color.BLUE);
        nodeTest.addStudent(Color.BLUE);
        nodeTest.addStudent(Color.BLUE);

        try {
            gameTest.checkIslandInfluence(2);
            assertEquals(nodeTest.getTowerColor(),TowerColor.EMPTY);
            assertEquals(exp5.getStopAvailable(),4);
        } catch (EndGameException e) {
            fail();
        }

        //Should calculate influence now
        try {
            assertEquals(nodeTest.isStopped(),false);
            gameTest.checkIslandInfluence(2);
        } catch (EndGameException e) {
            fail();
        }

        assertEquals(nodeTest.getTowerColor(),p1.getTowerColor());

        exp5.endEffect();
        assertEquals(gameTest.getActiveExpertCard(),null);


    }

    @DisplayName("Test Expert 6")
    @Test
    void expert6Test() throws FileNotFoundException {
        Expert6 exp6 = new Expert6(gameTest);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        gameTest.addPlayer("tu",DeckType.DRUID,TowerColor.BLACK);

        Player p1 = gameTest.getPlayersList().get(0);
        Player p2 = gameTest.getPlayersList().get(1);

        p1.getBoard().addToDiningTest(Color.BLUE);
        p2.getBoard().addToDiningTest(Color.RED);

        gameTest.checkInfluence(p1,Color.BLUE);
        gameTest.checkInfluence(p2,Color.RED);

        IslandList islandsTest = gameTest.getGameField();
        //Check for void note for test
        int i;
        Node testNode = islandsTest.getIslandNode(1);
        while(testNode.getStudents().size() != 0){
            testNode = testNode.getNextNode();
        }

        testNode.addStudent(Color.BLUE);
        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
            assertEquals(testNode.getTowerColor(),p1.getTowerColor());

        } catch (EndGameException e) {
            fail();
        }

        //Set the same influence for p2, influence shouldn't change
        testNode.addStudent(Color.RED);
        testNode.addStudent(Color.RED);

        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
        } catch (EndGameException e) {
            fail();
        }
        assertEquals(testNode.getTowerColor(),p1.getTowerColor());

        gameTest.coinHandler(p2,5);

        try {
            exp6.useCard(p2);
        } catch (NotEnoughCoins e) {
            fail();
        }

        assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp6.getClass().getName());
        //Recalculating influence, now p2 should build a tower
        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
        } catch (EndGameException e) {
            fail();
        }
        assertEquals(testNode.getTowerColor(),p2.getTowerColor());

        exp6.endEffect();
        assertEquals(gameTest.getActiveExpertCard(),null);
    }
}
