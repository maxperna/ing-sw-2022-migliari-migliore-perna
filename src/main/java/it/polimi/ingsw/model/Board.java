package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.*;


public class Board implements StudentManager {

    private int hallDimension;       //maximum number of players in the external hall depending by the num of players
    private int maxNumOfTowers;        //maximum number of towers depending by the number of players
    private TowerColor towerColor;      //color of the towe on the board
    private final UUID boardID;
    private ArrayList<Color> studentsOutside;    //list of student in the outer room
    private Map<Color,Integer> lectureHall;      //list of student for each color inside the main hall
    private Map<Color, Boolean> teachers;       //map to signal the presence of a teacher on the board
    private ArrayList<TowerColor> towers;  //number of towers on the board

    //Private constructor used by the factory
    private Board(){       //num of towers is a parameter which depends on the num of players
        this.boardID = UUID.randomUUID();         //thread safe auto generated ID
    }

    //Method used by the factory to create a new board
    public static Board createBoard(){
        return new Board();
    }

    public UUID getBoardID(){
        return this.boardID;
    }

    public ArrayList<Color> getStudentsOutside(){
        return studentsOutside;
    }
    //da modificare
    public int getNumOfTowers() {
        return towers.size();
    }

    //Method to get the presence of a teacher on the board
    public boolean getTeacher(Color color){
        return teachers.get(color);
    }
    //Method to add the teacher on the board
    public void addTeachers(Color color){
        teachers.put(color,true);
    }
    //Method to remove the teacher on the board
    public void removeTeacher(Color color){
        teachers.put(color, false);
    }
    //Method to move the student from the external hall to the internal one
    public void moveInside(Color color) throws NotOnBoardException{     //Not on board da implementare
        if(!studentsOutside.contains(color)) throw new NotOnBoardException();
        else{
            studentsOutside.remove(color);
            lectureHall.put(color,lectureHall.get(color)+1);       //add a student of a color after removing it

        }
    }
    //Method to move the student from the board to the island
    public void moveToIsland(Color color, IslandTile targetIsland) throws NotOnBoardException{
        if(!studentsOutside.contains(color)) throw new NotOnBoardException();
    }
    //Method to move a tower on an Island
    public void moveTower() throws EndGameException{
        if(towers.size() == 0) throw new EndGameException("Out of towers");
        else{
            towers.remove(towerColor);
        }
    }              //throw an exception which signals the end of the game
    //Method to add the tower on the board
    public void addTower(){
        towers.add(towerColor);
    }

    public void setStudentsOutside(ArrayList<Color> studentsOutside) {
        this.studentsOutside = studentsOutside;
    }

    public void setTowers(int maxNumOfTowers) {
        this.maxNumOfTowers = maxNumOfTowers;
    }

    public void setTowerColor(TowerColor towerColor){
        this.towerColor = towerColor;
    }

    public void setHallDimension(int hallDimension){
        this.hallDimension = hallDimension;
    }

    public int colorStudent(Color color){
        return 0;
    }

}
