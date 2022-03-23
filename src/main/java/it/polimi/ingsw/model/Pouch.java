package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Class Pouch, Singleton, contains 130 students at the beginning of the game, the only way to call a Pouch is to use getInstance
 * @author Miglia
 */
public class Pouch {

    private ArrayList<Student> students;

    //create an object of Pouch
    private static Pouch instance = null;

    /**
     * Constructor
     * private so that we cannot instantiate the class
     */
    private Pouch() {};

    /**
     * Getter, ensures that there will be only one Pouch
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

        return 0;
    }


}