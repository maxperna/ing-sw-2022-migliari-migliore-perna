package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Class Pouch, Singleton, contains 120 students at the beginning of the game, the only way to call a Pouch is to use getInstance
 * @author Alessio Migliore
 */
public class Pouch {

    private final ArrayList<Color> students = new ArrayList<>();
    public Pouch() {                                                                                                   //creates an instance of Pouch

        for (int i = 0; i < 24; i++) {
            students.add(Color.RED);
            students.add(Color.BLUE);
            students.add(Color.YELLOW);
            students.add(Color.PINK);
            students.add(Color.GREEN);
        }
        Collections.shuffle(students);                                                                                  //method to shuffle the students arraylist
    }


    /**
     * Method that provides an arrayList of students by selecting and then removing the first object in the arrayList
     *
     * @param arrayListLength number of students required by the calling method
     * @return ArrayList of objects to be moved outside the pouch
     * @throws NotEnoughStudentsException there are less than the required elements in pouch
     */
    public synchronized ArrayList<Color> randomDraw(int arrayListLength) throws NotEnoughStudentsException{
        ArrayList<Color> studentsToBeMoved = new ArrayList <>();                                                        //new arrayList containing the number of students required

        if (arrayListLength > students.size())                                                                          //checks that there are enough students in the pouch
            throw new NotEnoughStudentsException();
        else {
            for (int i = 0; i < arrayListLength; i++) {                                                                 //iterate until the number of required students is matched
                studentsToBeMoved.add(students.get(0));                                                                 //add the 1st student from the students arraylist to the new arrayList
                students.remove(0);                                                                               //remove the moved student from the total student count

            }
        }
        return studentsToBeMoved;
    }

    /**
     * method used to add students to the pouch from an external source
     * @param students is an arraylist containing the students that are going to be moved inside the pouch
     */
    public void addStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    /**
     * @return the number of remaining students inside the pouch
     */
    public int remainingStudents() {
        return students.size();
    }

}

