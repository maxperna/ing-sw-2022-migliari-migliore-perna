package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.StoppedIslandException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class Node, used to create a doubly circular linked list of nodes, each node contains an Arraylist of islands, initially created with only 1 island
 * can be considered as a superclass of IslandTile that contains pointers to create the linked list
 */
public class Node {
    private int ID;
    private boolean motherNature;
    private ArrayList<Color> students;                                                                                  //each node contains an arraylist of islands, the arraylist initially contains only one island object, but it will add new islands
    private Player mostInfluencePlayer;
    public TowerColor tower;
    private boolean stop;
    private int towerCounter;
    private Node next;                                                                                           //whenever a MergeIsland is called
    private Node prev;
                                                                                                  //da togliere, solo per il test

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

    /**
     * constructor for the Node class
     * @param ID is the nodeID
     */
    public Node(int ID, ArrayList<Color> students) {
        this.ID = ID;
        this.next = null;
        this.prev = null;
        this.mostInfluencePlayer = null;
        this.tower = TowerColor.EMPTY;
        this.motherNature = false;
        this.stop = false;
        this.towerCounter = 0;
        this.students = students;
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
    }

    /**
     * method to reset motherNature flag when motherNature leaves
     */
    public void resetMotherNature(){
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
        students.add(student);
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
}
