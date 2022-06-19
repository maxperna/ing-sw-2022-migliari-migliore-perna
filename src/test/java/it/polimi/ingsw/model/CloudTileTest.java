package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CloudTileTest {

//    @DisplayName("Testing getStudents should throw exception")
//    @Test
//    void getStudentsShouldThorwException(){
//        GameManager game = GameManager.getInstance();
//        game.initGame("TwoPlayers",false);
//        assertThrows(EmptyCloudException.class, () -> {
//            game.getGame(0).getCloudTiles().get(0).getStudents();
//        });
//        game.setNull();
//    }

    @DisplayName("Testing moveStudents should throw exception")
    @Test
    void moveStudents() throws EmptyCloudException {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
        ArrayList<Color> students = new ArrayList<>();
        try {
            students.addAll(game.getGame(0).getPouch().randomDraw(3));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
        game.getGame(0).getCloudTiles().get(0).setStudents(students);
        game.getGame(0).getCloudTiles().get(0).moveStudents();
        game.setNull();
    }

    @DisplayName("Testing geTileID should return the correct one")
    @ParameterizedTest
    @CsvSource ({"0", "1", "2", "3"})
    void getTileID(int ID) {
        GameManager game = GameManager.getInstance();
        game.initGame("FourPlayers",false);
        assertEquals(ID, game.getGame(0).getCloudTiles().get(ID).getTileID());
    }

    @DisplayName("Testing setStudents should throw exception")
    @Test
    void setStudents() throws EmptyCloudException {
        GameManager game = GameManager.getInstance();
        game.initGame("TwoPlayers",false);
        ArrayList<Color> students = new ArrayList<>();
        try {
           students.addAll(game.getGame(0).getPouch().randomDraw(3));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
        game.getGame(0).getCloudTiles().get(0).setStudents(students);
        assertEquals(students, game.getGame(0).getCloudTiles().get(0).getStudents());
        game.setNull();
    }

}