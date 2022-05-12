package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PouchTest {

    @ParameterizedTest
    @CsvSource ({"1", "10", "106"})
    void randomDraw(int input) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        ArrayList<Color> students = new ArrayList<>();
        try {
            students.addAll(game.getGame(0).getPouch().randomDraw(input));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
        assertEquals(input, students.size());
        assertEquals(120-input, game.getGame(0).getPouch().remainingStudents());
        game.setNull();
    }

    @ParameterizedTest
    @CsvSource ({"1", "10", "120"})
    void randomDrawShouldThrowException(int input) {
        GameManager game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);

        if(input == 120) {
            assertThrows(NotEnoughStudentsException.class, () -> {
                game.getGame(0).getPouch().randomDraw(input);
            });
        }
        else
            try {
            game.getGame(0).getPouch().randomDraw(input);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addStudents() {
        GameManager game = GameManager.getInstance();
        game.setNull();
        game = GameManager.getInstance();
        game.startGame("TwoPlayers",false);
        ArrayList<Color> students = new ArrayList<>();
        students.add(Color.RED);
        game.getGame(0).getPouch().addStudents(students);
        assertEquals(121, game.getGame(0).getPouch().remainingStudents());
    }

}