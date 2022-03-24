package it.polimi.ingsw.components;

import it.polimi.ingsw.Color;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.StudentManager;

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

    private Board(){

    }


}
