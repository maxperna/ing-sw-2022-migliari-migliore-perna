package it.polimi.ingsw.model;
import java.util.*;

/**
 * Class Cloud Tile, one for each player, contains MAX 3 students
 * @author Miglia
 */
public class CloudTile {

    private int tileID;
    private ArrayList<Student> students;

    /**
     * @return a string with the number and color of students on the tile, returns "empty" if there are none
     */
    public String getStudent() {
        String studentsString = "Empty";

        return studentsString;
    }

    /**
     * @return an ArrayList of three students on the cloud and deletes them from ArrayList<Student> students, returns null if there are none
     */
    public ArrayList<Student> moveStudents() {

        return new ArrayList<Student>();
    }
}
