package it.polimi.ingsw.model.gameField;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;
import org.jetbrains.annotations.TestOnly;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Alessio Migliore
 * class IsladNode, used to create a doubly circular linked list of nodes, each node contains an Arraylist of islands, initially created with only 1 island
 * can be considered as a superclass of IslandTile that contains pointers to create the linked list
 */
public class IslandNode implements Serializable {
    private final ArrayList<Color> students;
    transient private final PropertyChangeSupport support;
    private int ID;
    private boolean motherNature;
    transient private Player mostInfluencePlayer;
    private TowerColor tower;
    private boolean stop;            //put to true if stopped by assistant card #5
    private int towerCounter;
    transient private IslandNode next;
    transient private IslandNode prev;
    transient private boolean ignoreTower;


    public IslandNode(int ID) {
        this.ID = ID;
        this.next = null;
        this.prev = null;
        this.mostInfluencePlayer = null;
        this.tower = TowerColor.EMPTY;
        this.motherNature = false;
        this.stop = false;
        this.towerCounter = 0;
        this.students = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * @return the previous node pointed by this object
     */
    public IslandNode getPreviousNode() {
        return this.prev;
    }

    /**
     * method to set the previous node of a given node in the linked list
     *
     * @param prev is the node that has to point to the new node
     */
    public void setPreviousNode(IslandNode prev) {
        this.prev = prev;
    }

    /**
     * @return return the next node pointed by this object
     */
    public IslandNode getNextNode() {
        return this.next;
    }

    /**
     * method to set the pointer to the next node in the list
     *
     * @param next is the node that has to be pointed by the new node
     */
    public void setNextNode(IslandNode next) {
        this.next = next;
    }

    /**
     * @return the ArrayList of islands, used when addIslands is called by mergeIslands
     */
    public ArrayList<Color> getStudents() {
        return this.students;
    }

    public void setStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    /**
     * method to set motherNature parameter when the island is visited
     */
    public void setMotherNature() {
        this.motherNature = true;
        support.firePropertyChange("UpdateNode " + ID, false, true);
    }

    /**
     * method to reset motherNature flag when motherNature leaves
     */
    public void resetMotherNature() {
        this.motherNature = false;
    }

    public boolean checkMotherNature() {
        return motherNature;
    }

    public int getNodeID() {
        return this.ID;
    }

    public boolean isStopped() {
        return this.stop;
    }

    public void stopIsland() {
        this.stop = true;
    }

    public void removeStop() {
        this.stop = false;
    }

    /**
     * Method setTower, updates the tower attribute after tower construction or substitution
     */
    public void setTower() throws EndGameException {

        if (mostInfluencePlayer != null) {
            int oldCounter = towerCounter;
            this.tower = mostInfluencePlayer.getBoard().moveTower();
            towerCounter++;

            support.firePropertyChange("UpdateNode " + ID, oldCounter, towerCounter);
        }
    }

    /**
     * Method getTowerColor
     *
     * @return color of placed tower
     */
    public TowerColor getTowerColor() {
        return this.tower;
    }

    /**
     * method to count students of a given color inside an island
     *
     * @param color color of the students you want to count
     * @return the number of students of a given color
     */
    public Integer getColorInfluence(Color color) {
        return Collections.frequency(students, color);
    }

    /**
     * method to add student
     *
     * @param student pawn to be added on the island
     */
    public void addStudent(Color student) {
        ArrayList<Color> oldStudents = new ArrayList<>(students);
        students.add(student);
        support.firePropertyChange("UpdateNode " + ID, oldStudents, students);
    }

    public void mergeStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    public Player getMostInfluencePlayer() {
        return mostInfluencePlayer;
    }

    public void setMostInfluencePlayer(Player player) {
        this.mostInfluencePlayer = player;
    }

    public void decreaseNodeID() {
        ID--;
    }

    public int getNumberOfTowers() {
        if (!ignoreTower)
            return this.towerCounter;
        else
            return 0;
    }

    public void mergeTowers(int towers) {
        this.towerCounter += towers;
    }

    public void changeIgnoreTower() {
        ignoreTower = !ignoreTower;
    }

    @TestOnly
    public void setTowerTest(TowerColor color) {
        this.tower = color;
        this.towerCounter++;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setMotherNature(boolean value) {
        this.motherNature = value;
    }

}
