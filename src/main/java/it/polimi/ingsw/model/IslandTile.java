package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;

import java.util.ArrayList;


/**
 * Class IslandTile
 * @author Alessio Migliore
 */
public class IslandTile implements StudentManager {
    private final int tileID;
    private ArrayList<Color> students;
    private TowerColor tower;
    private Player mostInfluencePlayer;


    /**
     * Method getID
     * @return island identifier
     */
    public int getID() {
        return this.tileID;
    }

    /**
     * Method setTower, updates the tower attribute after tower construction or substitution
     *
     */
    public void setTower(Player activePlayer){
        try{
            this.tower = activePlayer.getBoard().moveTower();
        }
        catch (EndGameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method getTowerColor
     * @return color of placed tower
     */
    public TowerColor getTowerColor(){
        return this.tower;
    }

    /**
     * constructor
     * @param tileID ID to assign to island tile
     */
    public IslandTile(int tileID){
        this.tileID=tileID;
    }

    public ArrayList <Color> getStudents() {
        return students;
    }

    /**
     * method to count students of a given color inside an island
     * @param color color of the students you want to count
     * @return the number of students of a given color
     */
    public int colorStudent(Color color){
        int colorCounter = 0;
        if(students.contains(color)) {
            for (Color student : students) {                                           //iterates for all students
                if (student.equals(color))                                          //checks if the student's color matches with given color
                    colorCounter++;                                                 //increases color counter for that color
            }
        }
        return colorCounter;
    }

    /**
     * method to add student
     * @param student pawn to be added on the island
     */
    public void addStudent(Color student) {
        students.add(student);
    }

    public void setStudents(ArrayList <Color> students) {
        this.students = students;
    }

    public void setMostInfluencePlayer(Player mostInfluencePlayer) {
        this.mostInfluencePlayer = mostInfluencePlayer;
    }
}


