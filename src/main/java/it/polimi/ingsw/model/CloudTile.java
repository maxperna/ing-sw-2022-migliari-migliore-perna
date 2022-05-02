package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.EmptyCloudException;

import java.util.*;

/**
 * Class Cloud Tile, one for each player, contains MAX 3 students
 * @author Alessio Migliore
 */
public class CloudTile {

    private int tileID;
    private ArrayList<Color> students;

    /**
     * @return an Arraylist of Color that represents students on the cloud
     * @throws EmptyCloudException when there are no students on the cloud
     */
    public ArrayList<Color> getStudent() throws EmptyCloudException{
        if(students.isEmpty())
            throw new EmptyCloudException();
        else
            return students;
    }

    /**
     * @throws EmptyCloudException when there are no students on the cloud
     * @return an ArrayList of three students on the cloud and deletes them from ArrayList of the cloud
     */
    public synchronized ArrayList<Color> moveStudents() throws EmptyCloudException{
        if(students.isEmpty())
            throw new EmptyCloudException();
        else {
            ArrayList<Color> studentsReturned = new ArrayList<Color>();                                                 //creates an ArrayList in which all students are copied
            studentsReturned.addAll(students);
            students.removeAll(students);                                                                               //empties the ArrayList students
            return studentsReturned;                                                                                    //returns an ArrayList containing a copy of the previous students in ArrayList students
        }
    }
}
