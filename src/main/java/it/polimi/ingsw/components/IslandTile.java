package it.polimi.ingsw.components;

import it.polimi.ingsw.StudentManager;

import java.util.ArrayList;


/**
 * Class IslandTile
 * @author Alessio Migliore
 */
public class IslandTile implements StudentManager {
    private static int tileID;
    private ArrayList<Student> students;
    private MotherNature motherNature;
    private Tower tower;
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
    public Tower getTowerColor(){
        return this.tower/*.getColor()*/;
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


}
