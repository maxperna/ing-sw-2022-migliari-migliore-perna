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
    private String mostInfluencePlayer;


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
     * @param tileID
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
     * @return
     */
    public int colorStudent(Color color){
        int colorCounter = 0;
        for (Color student : this.getStudents()) {                                                                      //iterates for all students
                if (students.equals(color))                                                                             //checks if the student's color matches with given color
                    colorCounter++;                                                                                     //increases color counter for that color
            }
        return colorCounter;
    }

    /**
     * method to add student
     * @param student
     */
    public void addStudent(Color student) {
        students.add(student);
    }
}


