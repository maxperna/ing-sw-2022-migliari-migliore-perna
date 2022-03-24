package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.Map;


public class Board implements StudentManager {

    private int numberOfPlayer;
    private int boardID;
    private Player player;
    private ArrayList<Student> studentsOutside;
    private Map<Color, Teacher> teachers;
    private ArrayList<Tower> towers;

    public int getBoardID(){
        return this.boardID;
    }

    public void getTeachers(){

    }

    public void addTeachers(){

    }

    public void removeTeacher(){

    }

    public void moveInside(){

    }

    public void moveToIsland(){

    }

    public void moveTower(){

    }

    public void addTower(){

    }

    public Board(){

    }

    public int colorStudent(Color color){
        return 0;
    }

}
