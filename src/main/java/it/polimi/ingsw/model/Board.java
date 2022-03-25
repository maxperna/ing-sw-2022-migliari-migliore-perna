package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class Board implements StudentManager {

    private final int hallDimension;       //maximum number of players in the external hall depending by the num of players
    private final int numOfTower;        //maximum number of towers depending by the number of players
    private final UUID boardID;
    private ArrayList<Student> studentsOutside;
    private Map<Color, Teacher> teachers;
    private ArrayList<Tower> towers;

    //Private constructor used by the factory
    private Board(int hallDimension,int numOfTower){
        this.hallDimension = hallDimension;
        this.numOfTower = numOfTower;
        this.boardID = UUID.randomUUID();         //thread safe auto generated ID
    }

    //Method used by the factory to create a new board
    public static Board createBoard(int hallDimension,int numOfTower){
        return new Board(hallDimension,numOfTower);
    }

    public UUID getBoardID(){
        return this.boardID;
    }

    public ArrayList<Student> getStudentsOutside(){
        return studentsOutside;
    }

    public Map<Color, Teacher> getTeachers() {
        return teachers;
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    //Method to get the presence of a teacher on the board
    public boolean getTeachers(Color color){return true;}
    //Method to add the teacher on the board
    public void addTeachers(Teacher teacher){}
    //Method to remove the teacher on the board
    public void removeTeacher(Teacher teacher){}
    //Method to move the student from the external hall to the internal one
    public void moveInside(Student student){}
    //Method to move the student from the board to the island
    public void moveToIsland(Student student, IslandTile targetIsland){}
    //Method to move a tower on an Island
    public void moveTower(){}
    //Method to add the tower on the board
    public void addTower(){}



    public int colorStudent(Color color){
        return 0;
    }

}
