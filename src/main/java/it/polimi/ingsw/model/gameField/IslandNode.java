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
 * Class IslandNode, used to create a doubly circular linked list of nodes
 * @author Alessio Migliore
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


    /**
     * Default constructor
     * @param ID is the ID that will identify the island Node
     */
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
     * Method that is used to get the previous node in the list
     * @return the previous node pointed by this object
     */
    public IslandNode getPreviousNode() {
        return this.prev;
    }

    /**
     * Method to set the previous node of a given node in the linked list
     *
     * @param prev is the node that has to point to the new node
     */
    public void setPreviousNode(IslandNode prev) {
        this.prev = prev;
    }

    /**
     * Method that is used to get the next node in the list
     * @return return the next node pointed by this object
     */
    public IslandNode getNextNode() {
        return this.next;
    }

    /**
     * Method to set the pointer to the next node in the list
     * @param next is the node that has to be pointed by the new node
     */
    public void setNextNode(IslandNode next) {
        this.next = next;
    }

    /**
     * Method used to get the students on this node
     * @return the students set on this island
     */
    public ArrayList<Color> getStudents() {
        return this.students;
    }

    /**
     * Method used to place students on this island
     * @param students is an arraylist containing the students that will be placed on this node
     */
    public void setStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    /**
     * Method used to set motherNature parameter when the island is visited
     */
    public void setMotherNature() {
        this.motherNature = true;
        support.firePropertyChange("UpdateNode " + ID, false, true);
    }

    /**
     * Method used to reset motherNature flag when motherNature leaves
     */
    public void resetMotherNature() {
        this.motherNature = false;
    }

    /**
     * Methods used to check mother nature presence on this island
     * @return a boolean that indicates mother nature presence
     */
    public boolean checkMotherNature() {
        return motherNature;
    }

    /**
     * Method used to an island ID
     * @return the identifier of the current island
     */
    public int getNodeID() {
        return this.ID;
    }

    /**
     * Method used to get the stop status of an island
     * @return a boolean indicating whether the island is stopped or not
     */
    public boolean isStopped() {
        return this.stop;
    }

    /**
     * Method used to set the stop flag of this island to true
     */
    public void stopIsland() {
        this.stop = true;
    }

    /**
     * Method used to set the stop flag of this island to false
     */
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

    /**
     * Method called during a merge, add all students from another island to this one
     * @param students are the students from the merged island that will be moved inside this island
     */
    public void mergeStudents(ArrayList<Color> students) {
        this.students.addAll(students);
    }

    /**
     * Method used to get the player with the highest influence on this island
     * @return the player with the highest influence
     */
    public Player getMostInfluencePlayer() {
        return mostInfluencePlayer;
    }

    /**
     * Method used to set the most influence player on this island
     * @param player the player with the highest influence
     */
    public void setMostInfluencePlayer(Player player) {
        this.mostInfluencePlayer = player;
    }

    /**
     * Method called after a merge to reduce the id of all the islands remaining that had an id greater than the merged one
     */
    public void decreaseNodeID() {
        ID--;
    }

    /**
     * Method used to get the number of towers on this island (2+ in case of merged islands)
     * @return the number of towers
     */
    public Integer getNumberOfTowers() {
        if (!ignoreTower)
            return this.towerCounter;
        else
            return 0;
    }

    /**
     * Method used to add the number of towers from the island that is going to be merged onto the new super island
     * @param towers is  the number of towers that were on the old island that will be deleted after merge
     */
    public void mergeTowers(int towers) {
        this.towerCounter += towers;
    }

    /**
     * Method used to reset the ignoreTower flag
     */
    public void changeIgnoreTower() {
        ignoreTower = !ignoreTower;
    }

    @TestOnly
    public void setTowerTest(TowerColor color) {
        this.tower = color;
        this.towerCounter++;
    }

    /**
     * Method used to set a propertyChangeListener on this island
     * @param listener is the listener that will notify all the changes
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove a listener
     * @param listener listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
