package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.StoppedIslandException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.IslandTile;

import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class Node, used to create a doubly circular linked list of nodes, each node contains an Arraylist of islands, initially created with only 1 island
 * can be considered as a superclass of IslandTile that contains pointers to create the linked list
 */
public class Node {
    private ArrayList<IslandTile> islands;                                                                              //each node contains an arraylist of islands, the arraylist initially contains only one island object, but it will add new islands
    private Node next = null;                                                                                           //whenever a MergeIsland is called
    private Node prev = null;
    private Color mostInfluencePlayer;
    private boolean motherNature = false;
    private boolean stop=false;
    private int ID;                                                                                                     //da togliere, solo per il test

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
     * @param newIsland is the object that we insert in the ArrayList of IslandTile
     */
    public Node(IslandTile newIsland, int ID) {
        islands = new ArrayList<>();
        islands.add(newIsland);
        this.ID = ID; //da togliere, solo per il test
    }

    /**
     * basic constructor
     */
    public Node() {
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
    public ArrayList<IslandTile> getIslandTiles() {
        return this.islands;
    }

    /**
     * method to add the island(s) of a Node inside another one, called to merge two islands (not tested)
     * @param islandsToBeAdded is an ArrayList of IslandTile that will be included in the ArrayList of the Node that will remain
     */
    public void addIslands(ArrayList<IslandTile> islandsToBeAdded) {
        this.islands.addAll(0, islandsToBeAdded);
    }

    /**
     * method used to update mostInfluencePlayer on this Node by calling the method getMostInfluence
     */
    public void setMostInfluencePlayer() {
        try {
            this.mostInfluencePlayer = this.getMostInfluence();
        } catch (StoppedIslandException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to control the dominant color on an island
     * @return the color of the highest amount of students on the island
     */
    public Color getMostInfluence() throws StoppedIslandException {
        if(!this.isStopped()) {
            Color maxColor = null;                                                                                          //declare dominant color as null
            int maxStudents = 0;                                                                                            //declare max students of dominant color as 0
            int colorCounter = 0;
            for (Color actualColor : Color.values()) {                                                                      //iterates for all colors of students
                for (IslandTile island : this.islands) {                                              //iterates for all islands in the node//creates a local copy of all students in the visited island
                    for (Color actualStudent : island.getStudents()) {                                           //iterates for all students in previously declared ArrayList
                        if (actualStudent.equals(actualColor)) {
                            colorCounter++;                                                             //checks if the student's color matches with current color
                        }                                                       //increases color counter for that color
                    }
                }
                if (maxStudents < colorCounter) {                                                                       //updates dominant color and number of dominant students values
                    maxStudents = colorCounter;
                    maxColor = actualColor;
                }
                colorCounter=0;
            }
            return maxColor;                                                                                            //returns dominant color
        }
        else throw new StoppedIslandException();

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
}
