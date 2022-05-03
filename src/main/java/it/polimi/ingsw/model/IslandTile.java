package it.polimi.ingsw.model;

import it.polimi.ingsw.*;

import java.util.ArrayList;


/**
 * Class IslandTile
 * @author Alessio Migliore
 */
public class IslandTile implements StudentManager {
    private final int tileID;
    private ArrayList<Color> students;
    private MotherNature motherNature;
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
    public void setTower(){
    }

    /**
     * Method getTowerColor
     * @return color of placed tower
     */
    public TowerColor getTowerColor(){
        return this.tower;
    }

    /**
     * Method setMostInfluence, checks the color of the highest number of students placed on the island
     *
     */
    public void setMostInfluencePlayer(){

    }

    public String getMostInfluencePlayer(){
        return this.mostInfluencePlayer;
    }

    public IslandTile(int tileID){
        this.tileID=tileID;
    }

    public void setStudents(ArrayList <Color> students) {
        this.students = students;
    }

    public ArrayList <Color> getStudents() {
        return students;
    }

    public int colorStudent(Color color){
        return 0;
    }




}


