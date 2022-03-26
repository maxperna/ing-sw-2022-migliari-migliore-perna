package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Class Pouch, Singleton, contains 130 students at the beginning of the game, the only way to call a Pouch is to use getInstance
 * @author Alessio Migliore
 */
public class Pouch {

    private ArrayList<Student> students;

    //create an object of Pouch
    private static Pouch instance = null;

    /**
     * Constructor
     * private so that we cannot instantiate the class, creates all students objects in an ArrayList and then shuffles it
     */
    private Pouch() {
        this.students = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            Student student = new Student(Color.RED);
            students.add(student);
        }
        for (int i = 0; i < 26; i++) {
            Student student = new Student(Color.GREEN);
            students.add(student);
        }
        for (int i = 0; i < 26; i++) {
            Student student = new Student(Color.PINK);
            students.add(student);
        }
        for (int i = 0; i < 26; i++) {
            Student student = new Student(Color.YELLOW);
            students.add(student);
        }
        for (int i = 0; i < 26; i++) {
            Student student = new Student(Color.BLUE);
            students.add(student);
        }

        Collections.shuffle(students);                                                   //method to shuffle the students arraylist

    }

    /**
     * Getter, ensures that there will be only one Pouch
     *
     * @return Pouch
     */
    public static Pouch getInstance() {
        if (instance == null)
            instance = new Pouch();

        return instance;
    }

    /**
     * @return number(int) of students left inside the bag
     */
    public int getNumberOfStudents() {
        return students.size();
    }

    /**
     * Method that provides an arrayList of students by selecting and then removing the first object in the arrayList
     *
     * @param arrayListLength
     * @return (ArrayList<Student>) of objects to be moved outside the pouch
     */
    public synchronized ArrayList<Student> randomDraw(int arrayListLength) {
        ArrayList<Student> studentsToBeMoved = new ArrayList<Student>();                   //new arrayList containing the number of students required
        if (arrayListLength > this.getNumberOfStudents()) {                                //checks that there are enough students in the pouch
            ArrayList<Student> EmptyArray = new ArrayList<>();
            return EmptyArray;
        }
        else {
            for (int i = 0; i < arrayListLength; i++) {                                    //iterate until the number of required students is matched
                studentsToBeMoved.add(students.get(0));                                    //add the 1st student from the students arraylist to the new arrayList
                students.remove(0);                                                  //remove the moved student from the total student count
            }
        }
        return studentsToBeMoved;
    }

    /**
     *
     * @param color
     * @return (int) counter that contains the number of objects inside pouch with the attribute Color
     */
    public int getColor(Color color) {
        int counter = 0, index = 0;
        while (index < this.getNumberOfStudents()) {
            if (students.get(index).getColor().equals(color))
                counter++;
            index++;
        }
        return counter;
    }
}
