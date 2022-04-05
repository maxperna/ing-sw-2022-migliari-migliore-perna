package it.polimi.ingsw.CircularLinkedList;

import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.MotherNature;

import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class Node, used to create a doubly circular linked list of nodes, each node contains an Arraylist of islands, initially created with only 1 island
 * can be considered as a superclass of IslandTile that contains pointers to create the linked list
 */
public class Node {
    public ArrayList<IslandTile> islands;                                                                               //each node contains an arraylist of islands, the arraylist initially contains only one island object, but it will add new islands
    private Node next=null;                                                                                             //whenever a MergeIsland is called
    private Node prev=null;
    private String mostInfluencePlayer;
    private MotherNature motherNature;

    /**
     * method to set the previous node of a given node in the linked list
     * @param prev is the node that has to point to the new node
     */
    public void setPreviousNode(Node prev){
        this.prev = prev;
    }

    /**
     * method to set the pointer to the next node in the list
     * @param next is the node that has to be pointed by the new node
     */
    public void setNextNode(Node next){
        this.next = next;
    }

    /**
     * constructor for the Node class
     * @param newIsland is the object that we insert in the ArrayList of IslandTile
     */
    public Node(IslandTile newIsland){
        islands = new ArrayList<>();
        islands.add(newIsland);
    }

    /**
     * basic constructor
     */
    public Node(){}

    /**
     *
     * @return the prevoius node pointed by this object
     */
    public Node getPreviousNode(){
        return this.prev;
    }

    /**
     *
     * @return return the next node pointed by this object
     */
    public Node getNextNode(){
        return this.next;
    }

    /**
     *
     * @return the ArrayList of islands, used when addIslands is called by mergeIslands
     */
    public ArrayList<IslandTile> getIslands(){
        return this.islands;
    }

    /**
     * method to add the island(s) of a Node inside another one, called to merge two islands (not tested)
     * @param islandsToBeAdded is an ArrayList of IslandTile that will be included in the ArrayList of the Node that will remain
     */
    public void addIslands(ArrayList<IslandTile> islandsToBeAdded){
        this.islands.addAll(0, islandsToBeAdded);
    }

}
