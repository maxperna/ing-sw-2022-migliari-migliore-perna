package it.polimi.ingsw.gameField;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;
import org.jetbrains.annotations.TestOnly;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class Node, used to create a doubly circular linked list of nodes, each node contains an Arraylist of islands, initially created with only 1 island
 * can be considered as a superclass of IslandTile that contains pointers to create the linked list
 */
public class Node implements Serializable {
    private int ID;
    private boolean motherNature;
    private final ArrayList<Color> students;
    private Player mostInfluencePlayer;
    private TowerColor tower;
    transient private boolean stop;            //put to true if stopped by assistant card #5
    private int towerCounter;
    transient private Node next;
    transient private Node prev;
    transient private boolean ignoreTower;
    transient private final PropertyChangeSupport support;


    /**
     * method to set the previous node of a given node in the linked list
     * @param prev is the node that has to point to the new node
     */
    public void setPreviousNode(Node prev) {
        this.prev = prev;
    }

    /**
     * method to set the pointer to the next node in the list
     * @param next is the node that has to be pointed by the new node
     */
    public void setNextNode(Node next) {
        this.next = next;
    }

    public Node(int ID) {
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
    public Node getPreviousNode() {
        return this.prev;
    }

    /**
     * @return return the next node pointed by this object
     */
    public Node getNextNode() {
        return this.next;
    }

    /**
     * @return the ArrayList of islands, used when addIslands is called by mergeIslands
     */
    public ArrayList<Color> getStudents() {
        return this.students;
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
    public void resetMotherNature(){
        this.motherNature = false;
        support.firePropertyChange("UpdateNode " + ID, true, false);
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

    public void removeStop(){
        this.stop = false;
    }

    /**
     * Method setTower, updates the tower attribute after tower construction or substitution
     *
     */
    public void setTower() throws EndGameException {

        if(mostInfluencePlayer != null) {
            int oldCounter = towerCounter;
            this.tower = mostInfluencePlayer.getBoard().moveTower();
            towerCounter++;

            support.firePropertyChange("UpdateNode " + ID, oldCounter, towerCounter);
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
     * method to count students of a given color inside an island
     * @param color color of the students you want to count
     * @return the number of students of a given color
     */
    public int getColorInfluence(Color color){
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
        ArrayList<Color> oldStudents = new ArrayList<>(students);
        students.add(student);
        support.firePropertyChange("UpdateNode " + ID, oldStudents, students);
    }

    public void setMostInfluencePlayer(Player player) {
        this.mostInfluencePlayer = player;
    }

    public void mergeStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    public Player getMostInfluencePlayer() {
        return mostInfluencePlayer;
    }

    public void decreaseNodeID() {
        ID--;
    }

    public int getNumberOfTowers() {
        if(!ignoreTower)
                return this.towerCounter;
        else
            return 0;
    }

    public void mergeTowers(int towers) {
        this.towerCounter+=towers;
    }

    public void changeIgnoreTower(){
        ignoreTower = !ignoreTower;
    }

    @TestOnly
    public void setTowerTest (TowerColor color) {
        this.tower = color;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setStudents(ArrayList<Color> students){
        this.students.addAll(students);
    }

}
