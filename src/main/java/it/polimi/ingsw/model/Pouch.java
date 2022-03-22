package it.polimi.ingsw.model;

import java.util.ArrayList;

//Singleton, contains 130 students at the beginning of the game, the only way to call a Pouch is to use getInstance
public class Pouch {

    private ArrayList<Student> students;

    //create an object of Pouch
    private static Pouch instance = null;

    //private constructor so that we cannot instantiate the class
    private Pouch() {};

    //returns the only available object
    public static Pouch getInstance() {
        if (instance == null)
            instance = new Pouch();

        return instance;
    }

    //returns the number of students left inside the bag
    public int getNumberOfStudents() {

        return 0;
    }


}