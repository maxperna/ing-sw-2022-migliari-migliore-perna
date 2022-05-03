package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameManagerTest {

    private static Stream<Arguments> provideParameters() {

        Random random = new Random();
        int randomNumber = random.nextInt(11 - 1 + 1) + 1;

        ArrayList<Color> empty = new ArrayList<>();

        ArrayList<Color> studentToBePlaced = new ArrayList<>();
        for (int i = 0; i < randomNumber; i++) {
            studentToBePlaced.add(Color.RED);
        }

        return Stream.of(

                Arguments.of(randomNumber, studentToBePlaced),
                Arguments.of(randomNumber + 1, studentToBePlaced),
                Arguments.of(randomNumber - 1, studentToBePlaced),
                Arguments.of(0, studentToBePlaced),
                Arguments.of(0, empty)
        );
    }

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers", "...");
    }

    @DisplayName("Testing startGame method...")
    @ParameterizedTest
    @MethodSource("gameModeList")
    void startGame(String gameMode) {

        try {
            GameManager.getInstance().startGame(gameMode,false);
            assertNotNull(GameManager.getInstance().getGamesList());

            for (int i = 0; i < GameManager.getInstance().getGamesList().size(); i++) {

                int motherNatureTile = 0;

                //ChecksGameField
                assertNotNull(GameManager.getInstance().getGame(i).getGameField());


                //Checks where MotherNature is
                for(int j = 1; j <= Game.MAX_TILE; j ++)
                {
                    if(GameManager.getInstance().getGame(i).getGameField().getIslandNode(j).checkMotherNature())
                        motherNatureTile = j;

                }

                if(motherNatureTile == 0)
                    System.out.println("MotherNature not Found\n");

                if(motherNatureTile <= 6)
                    assertEquals(0, GameManager.getInstance().getGame(i).getGameField().getStudentsFromIslandNode(motherNatureTile + 6).size());

                if(motherNatureTile > 6)
                    assertEquals(0, GameManager.getInstance().getGame(i).getGameField().getStudentsFromIslandNode(motherNatureTile - 6).size());


                switch (GameManager.getInstance().getGame(i).getPlayersList().size()) {

                    case 2: {

                        //Checks Players
                        assertEquals(TwoPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                        for (Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList()) {
                            assertEquals(TwoPlayers.maxStudentHall, currentPlayer.getBoard().getEntryRoom().size());
                            assertEquals(TwoPlayers.maxTowers, currentPlayer.getBoard().getNumOfTowers());
                        }

                        //Checks CloudTile
                        assertEquals(TwoPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getCloudTiles().size());

                        break;
                    }

                    case 4: {

                        //Checks Players
                        assertEquals(FourPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                        for (Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList()) {
                            assertEquals(FourPlayers.maxStudentHall, currentPlayer.getBoard().getEntryRoom().size());
                            assertEquals(FourPlayers.maxTowers, currentPlayer.getBoard().getNumOfTowers());
                        }

                        //Checks CloudTile
                        assertEquals(FourPlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getCloudTiles().size());


                        break;
                    }

                    case 3: {

                        //Checks Players
                        assertEquals(ThreePlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getPlayersList().size());

                        for (Player currentPlayer : GameManager.getInstance().getGame(i).getPlayersList()) {
                            assertEquals(ThreePlayers.maxStudentHall, currentPlayer.getBoard().getEntryRoom().size());
                            assertEquals(ThreePlayers.maxTowers, currentPlayer.getBoard().getNumOfTowers());
                        }

                        //Checks CloudTile
                        assertEquals(ThreePlayers.numberOfPlayers, GameManager.getInstance().getGame(i).getCloudTiles().size());

                        break;
                    }

                }

            }
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument " + "'" + gameMode + "'");
        }

    }

    @DisplayName("Testing getInstance...")
    @Test
    void getInstance() {

        assertNotNull(GameManager.getInstance());
    }

    @DisplayName("Testing getGameList...")
    @Test
    void getGamesList() {

        assertNotNull(GameManager.getInstance().getGamesList());
    }

    @ParameterizedTest
    @CsvSource({"TwoPlayers,100", "ThreePlayers,0", "FourPlayers,1"})
    void getGame(String gameMode, int index) {

        try {
            Game game = GameManager.getInstance().getGame(index);
            assertNotNull(game);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound");
        }

        GameManager.getInstance().startGame(gameMode,false);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void shouldDrawFromPool(int testNumber, ArrayList<Color> sampleArray) {

        //System.out.println("testNUmber: " + testNumber + " Array: " + sampleArray + "\n");

        ArrayList<Color> draw;
        ArrayList<Color> testArray = new ArrayList<>(sampleArray);

        try {
            draw = GameManager.drawFromPool(testNumber, testArray);
            assertEquals(testNumber, draw.size());

        } catch (NotEnoughElements e) {
            System.out.println("Not enough elements\n");
        }

    }


}