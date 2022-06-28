package it.polimi.ingsw.experts;

import it.polimi.ingsw.controller.TurnLogic;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.model.gameField.IslandList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertsCardTest {

    GameManager GM = GameManager.getInstance();
    Game gameTest = GM.initGame("TwoPlayers",true);

    @Test
    @DisplayName("Expert1 test ")
    void expert1Test() throws FileNotFoundException {
        Expert1 exp1 = new Expert1(gameTest);
        ArrayList<Color> studentsOnCard = exp1.getStudentsOnCard();
        IslandNode nodeTest = gameTest.getGameField().getIslandNode(2);
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
            assertNull(gameTest.getActiveExpertCard());
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

        assertTrue(p1.getBoard().getTeacher(Color.BLUE));
        assertTrue(p1.getBoard().getTeacher(Color.RED));

        p2.getBoard().addToDiningTest(Color.RED);
        p2.getBoard().addToDiningTest(Color.BLUE);

        gameTest.checkInfluence(p2,Color.RED);
        gameTest.checkInfluence(p2,Color.BLUE);

        assertFalse(p2.getBoard().getTeacher(Color.BLUE));
        assertFalse(p2.getBoard().getTeacher(Color.RED));

        try {
            exp2.useCard(p2);
            //Check the effective swap of professor
            assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp2.getClass().getName());
            assertTrue(p2.getBoard().getTeacher(Color.BLUE));
            assertTrue(p2.getBoard().getTeacher(Color.RED));

            assertFalse(p1.getBoard().getTeacher(Color.BLUE));
            assertFalse(p1.getBoard().getTeacher(Color.RED));

            exp2.endEffect();

            assertNull(gameTest.getActiveExpertCard());
            assertFalse(p2.getBoard().getTeacher(Color.BLUE));
            assertFalse(p2.getBoard().getTeacher(Color.RED));

            assertTrue(p1.getBoard().getTeacher(Color.BLUE));
            assertTrue(p1.getBoard().getTeacher(Color.RED));



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

        IslandNode testNode = gameTest.getGameField().getIslandNode(2);

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
        } catch (IllegalMove e) {
            e.printStackTrace();
        }

        exp3.endEffect();
        assertNull(gameTest.getActiveExpertCard());
    }

    @Test
    @DisplayName("Test expert 4")
    void expert4Test() throws FileNotFoundException {
        GameManager.setNull();
        Game gameTest = GameManager.getInstance().initGame("TwoPlayers",true);
        Expert4 exp4 = new Expert4(gameTest);

        TurnLogic turnLogicTest = new TurnLogic(gameTest);

        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 = gameTest.getPlayersList().get(0);

        gameTest.coinHandler(p1,5);

        try {
            turnLogicTest.setPlayedCard(3,p1);  //cart 3 maximum MN movement = 2
        } catch (CardAlreadyPlayed | EndGameException | NonexistentCard e) {
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

        if(actualNodeMN+4 > 12)
            actualNodeMN = actualNodeMN-12;

        assertEquals(actualNodeMN+4,gameTest.getGameField().getMotherNature().getNodeID());

        exp4.endEffect();
        assertNull(gameTest.getActiveExpertCard());
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

        IslandNode nodeTest = gameTest.getGameField().getIslandNode(2);

        //Check island is not stopped
        assertFalse(nodeTest.isStopped());

        try {
            exp5.useCard(p1,2);
            assertEquals(exp5.getStopAvailable(),3);
            assertTrue(nodeTest.isStopped());
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
            assertFalse(nodeTest.isStopped());
            gameTest.checkIslandInfluence(2);
        } catch (EndGameException e) {
            fail();
        }

        assertEquals(nodeTest.getTowerColor(),p1.getTowerColor());

        exp5.endEffect();
        assertNull(gameTest.getActiveExpertCard());


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
        IslandNode testNode = islandsTest.getIslandNode(1);
        while(testNode.getStudents().size() != 0){
            testNode = testNode.getNextNode();
        }

        testNode.addStudent(Color.BLUE);
        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
            assertEquals(p1.getTowerColor(), testNode.getTowerColor());

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
        assertEquals(p1.getTowerColor(), testNode.getTowerColor());

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
        assertNull(gameTest.getActiveExpertCard());
    }

    @Test
    @DisplayName("Expert 7 test")
    void expert7Test() throws FileNotFoundException {
        Expert7 exp7 = new Expert7(gameTest);
        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.WHITE);
        Player p1 =gameTest.getPlayersList().get(0);

        gameTest.coinHandler(p1,5);

        ArrayList<Color> initialStudentsBoard = new ArrayList<>(p1.getBoard().getEntryRoom());
        ArrayList<Color>  initialStudentsCard = new ArrayList<>(exp7.getStudentsOnCard());
        //Getting 3 students to swap
        ArrayList<Color> studentsToSwap = new ArrayList<>();
        ArrayList<Color> studentsToPick = new ArrayList<>();
        for(int i=0;i<3;i++){
            //Manually simulating a swap
            Color colorBoard = p1.getBoard().getEntryRoom().get(i);
            Color colorCard = exp7.getStudentsOnCard().get(i);

            studentsToSwap.add(colorBoard);
            initialStudentsBoard.remove(colorBoard);
            initialStudentsCard.add(colorBoard);

            studentsToPick.add(colorCard);
            initialStudentsBoard.add(colorCard);
            initialStudentsCard.remove(colorCard);
        }

        try {
            exp7.useCard(p1,studentsToPick, studentsToSwap);
        } catch (NotEnoughCoins | IllegalMove e) {
            fail();
        }

        //Check if students are the same
        assertEquals(p1.getBoard().getEntryRoom().size(),7);
        assertEquals(exp7.getStudentsOnCard().size(),6);

        ArrayList<Color> mockEntryRoom = new ArrayList<>(p1.getBoard().getEntryRoom());
        //Check the correctness of the board
        for (Color color:initialStudentsBoard){
            assertTrue(mockEntryRoom.remove(color));
        }

        //Check the correctness of the cards
        ArrayList<Color> mockStudentsOnCard = new ArrayList<>(exp7.getStudentsOnCard());
        for(Color color:initialStudentsCard){
            assertTrue(mockStudentsOnCard.remove(color));
        }

        exp7.endEffect();
        assertNull(gameTest.getActiveExpertCard());

        gameTest.coinHandler(p1,3);

        //Testing exception
        studentsToPick.clear();
        //Too much students
        for(int i=0;i<4;i++){
            studentsToPick.add(p1.getBoard().getEntryRoom().get(i));
        }
        try {
            exp7.useCard(p1,studentsToPick,studentsToSwap);
        } catch (NotEnoughCoins e) {
            fail();
        } catch (IllegalMove e) {
            assert true;
        }

        //The list doesn't match in size
        studentsToPick.remove(3);
        studentsToSwap.remove(2);

        try {
            exp7.useCard(p1,studentsToSwap,studentsToPick);
        } catch (NotEnoughCoins e) {
            fail();
        } catch (IllegalMove e) {
            assert true;
        }

    }

    @Test
    @DisplayName("Testing expert 9")
    void expert9Test() throws FileNotFoundException {
        gameTest.addPlayer("io",DeckType.DRUID,TowerColor.WHITE);
        gameTest.addPlayer("tu",DeckType.SAGE,TowerColor.BLACK);

        IslandNode testNode = gameTest.getGameField().getIslandNode(2);

        Player p1 = gameTest.getPlayersList().get(0);
        Player p2 = gameTest.getPlayersList().get(1);

        Expert9 exp9 = new Expert9(gameTest);

        //Setting same conditions over influence calculus

        Color colorP1;
        Color colorP2;
        try{
            Color colorAlreadyPresent = testNode.getStudents().get(0);
            if(colorAlreadyPresent.equals(Color.RED))
            {
                colorP1 = Color.PINK;
            }
            else {
                colorP1 = Color.RED;
            }
            if (colorAlreadyPresent.equals(Color.BLUE)) {
                colorP2 = Color.GREEN;
            }
            else{
                colorP2 = Color.BLUE;
            }
        }
        catch (IndexOutOfBoundsException e){
            colorP1 = Color.RED;
            colorP2 = Color.BLUE;
        }

        p1.getBoard().addToDiningTest(colorP1);
        p2.getBoard().addToDiningTest(colorP2);

        gameTest.checkInfluence(p1,colorP1);
        gameTest.checkInfluence(p2,colorP2);
        //Setting students on island
        testNode.addStudent(colorP1);
        testNode.addStudent(colorP1);

        testNode.addStudent(colorP2);

        gameTest.coinHandler(p2,3);

        try {
            exp9.useCard(p2,colorP1);
        } catch (NotEnoughCoins e) {
            fail();
        }

        assertEquals(gameTest.getActiveExpertCard().getClass().getName(),exp9.getClass().getName());

        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
        } catch (EndGameException e) {
            fail();
        }

        //Trying card normally
        assertEquals(p2.getTowerColor(), testNode.getTowerColor());  //p2 build tower
        exp9.endEffect();
        assertNull(gameTest.getActiveExpertCard());

        //Trying if card swap color and tower
        gameTest.coinHandler(p1,5);
        try {
            exp9.useCard(p1,colorP2);
        } catch (NotEnoughCoins e) {
            fail();
        }

        try {
            gameTest.checkIslandInfluence(testNode.getNodeID());
        } catch (EndGameException e) {
            fail();
        }

        assertEquals(p1.getTowerColor(), testNode.getTowerColor());


    }

    @Test
    @DisplayName("Expert 9 test ")
    void expert10Test() throws FileNotFoundException {
        gameTest.addPlayer("io",DeckType.SAGE,TowerColor.BLACK);

        Player p1 = gameTest.getPlayersList().get(0);

        Expert10 exp10 = new Expert10(gameTest);

        p1.getBoard().addToDiningTest(Color.BLUE);
        p1.getBoard().addToDiningTest(Color.RED);

        ArrayList<Color> studentsInside = new ArrayList<>(Arrays.asList(Color.BLUE,Color.RED));

        ArrayList<Color> mockEntryRoom = new ArrayList<>(p1.getBoard().getEntryRoom());
        mockEntryRoom.removeAll(studentsInside);

        ArrayList<Color> studentsEntry = new ArrayList<>();
        studentsEntry.add(mockEntryRoom.get(0));
        studentsEntry.add(mockEntryRoom.get(1));

        gameTest.coinHandler(p1,5);

        try {
            exp10.useCard(p1,studentsEntry,studentsInside);
        } catch (NotEnoughCoins e) {
            fail();
        } catch (NotOnBoardException e) {
            e.printStackTrace();
        } catch (IllegalMove e) {
            e.printStackTrace();
        }

        mockEntryRoom.clear();
        mockEntryRoom.addAll(p1.getBoard().getEntryRoom());
        mockEntryRoom.removeAll(studentsEntry);

        for(Color color: studentsEntry){
            assertNotEquals(p1.getBoard().getDiningRoom().get(color),0);
        }

        for(Color color:studentsInside){
            assertTrue(mockEntryRoom.remove(color));
        }


    }
}
