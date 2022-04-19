package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughElements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @ParameterizedTest
    @MethodSource ("provideParameters")
    void shouldDrawFromPool(int testNumber, ArrayList<Color> sampleArray) {

        System.out.println("testNUmber: " + testNumber + " Array: " + sampleArray + "\n");

        ArrayList<Color> draw;
        ArrayList<Color> testArray = new ArrayList<>(sampleArray);

        try {
            draw = GameManager.drawFromPool(testNumber, testArray);
            assertEquals(testNumber, draw.size());

        } catch (NotEnoughElements e) {
            System.out.println("Not enough elements\n");
        }

    }

    @Test
    void getGameID() {

        Game game = new Game(2,1,1);

        assertNotNull(game.getGameID());
    }

    private static Stream<Arguments> provideParameters(){

        Random random = new Random();
        int randomNumber = random.nextInt(11 - 1 + 1) + 1;

        ArrayList<Color> empty = new ArrayList<>();

            ArrayList<Color> studentToBePlaced = new ArrayList <>();
            for (int i = 0; i < randomNumber; i++) {
                studentToBePlaced.add(Color.RED);
            }

        return Stream.of(

                Arguments.of(randomNumber, studentToBePlaced),
                Arguments.of(randomNumber + 1,studentToBePlaced),
                Arguments.of(randomNumber - 1,studentToBePlaced),
                Arguments.of(0,studentToBePlaced),
                Arguments.of(0, empty)
        );
    }
}