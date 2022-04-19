package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.IslandTile;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class IslandList, composed of up to 12 objects Node, linked in a doubly circular linked list (DCLL)
 */


public class IslandList {
    Node head;

    /**
     * method used to add all Nodes containing an ArrayList of IslandTiles in an initially doubly linked list, when the list has all 12 Nodes, the last element points to the end, making this list circular
     *
     * @param islands that will be part of the nodes (the node can be seen as a superclass of IslandTile, containing all references and methods to make a DCLL)
     */
    public void addIslands(ArrayList<IslandTile> islands) {
        for (int i = 0; i < islands.size(); i++) {
            Node newIsland = new Node(islands.get(i));
            newIsland.setNextNode(this.head);                                                                           //insert new node before all the nodes in the linked list
            newIsland.setPreviousNode(null);

            if (head != null)
                head.setPreviousNode(newIsland);                                                                        //if linked list is not empty, link the head node to the new node

            head = newIsland;                                                                                           //set the new node as the head of the list
            int counter = this.islandCounter(head);
            Node lastNode = this.lastNode(head);

            if (counter == 11) {                                                                                        //counter.equals(11) gives problem since int is a primitive type, have to use this one I suppose
                lastNode.setNextNode(head);
                head.setPreviousNode(lastNode);                                                                         //if the linked list contains 12 elements, link the last element to the head, to make a circular linked list
            }
        }
    }

    /**
     * basic constructor
     */
    public IslandList() {
    }


    /**
     * method to merge islands, it transfers the ArrayList IslandTile from the node that we eliminate to the one we call the method on
     *
     * @param newMergedIsland is the island that remains in the linked list
     * @param islandToMerge   is the island whose information is going to be moved inside the other island, this one will be cancelled by garbage collector
     * @throws EndGameException when there are exactly 3(?) islands left inside the list
     */
    public void mergeIslands(int newMergedIsland, int islandToMerge) throws EndGameException {                          //not "tested"
        Node newIsland = this.moveToIsland(newMergedIsland);
        Node oldIsland = this.moveToIsland(islandToMerge);
        newIsland.addIslands(oldIsland.getIslands());                                                                   //instruction to add islands from the node we intend to merge to the final one
        newIsland.setNextNode(oldIsland.getNextNode());                                                                 //new island's next node becomes merged island's next node
        newIsland.getNextNode().setPreviousNode(newIsland);                                                             //merged island's next node stores into previous node the pointer to the new node
        if (this.islandCounter(this.head) == 3)
            throw new EndGameException();
    }

    /**
     * method used to count elements inside a linked list (circular or not)
     *
     * @param head receives in input the starting node of the list
     * @return counter, an int that contains the number of nodes inside the list
     */
    public int islandCounter(Node head) {
        int counter = 0;                                                                                                //counter variable to count the number of nodes inside the linked list
        Node startingNode = head;                                                                                       //new node to move through the linked list

        while (startingNode.getNextNode() != null && startingNode.getNextNode() != head) {                              //cycle to move through the linked list, used to check number of nodes inside the list with counter variable
            counter++;
            startingNode = startingNode.getNextNode();
        }
        return counter;
    }

    /**
     * method that returns the last node in a list (circular or not)
     *
     * @param head receives in input the starting node of the list
     * @return the last node in the list received in input
     */
    public Node lastNode(Node head) {
        Node startingNode = head;
        while (startingNode.getNextNode() != null && startingNode.getNextNode() != head)                                //checks that the next node in order is not empty (linear list) and it's not the starting node (circular list)
            startingNode = startingNode.getNextNode();
        return startingNode;
    }

    /**
     * method to get influence, missing the part that adds tower influence
     *
     * @param node is the island (or super island) we want to know about
     * @return Color containing dominant color
     */
    public Color getInfluence(Node node) {                                                                              //method to check influence on an island or on merged islands
        return node.getMostInfluence();                                                                                 //returns the dominant color in the island
    }

    /**
     * method to add a student to a given island
     *
     * @param islandID (0-11), identifies the island
     * @param student  to be added
     * @throws InvalidParameterException when islandID is invalid
     */
    public void addStudent(int islandID, Color student) throws InvalidParameterException {                              //method that adds a single student to a specific IslandTIle
        if (islandID < 1 || islandID > 12)                                                                              //checks that the islandID is valid
            throw new InvalidParameterException();

        Node startingNode = this.head;                                                                                  //set the startingNode
        while (startingNode.getNextNode() != head) {                                                                    //checks that we have nodes left to visit
            for (IslandTile island : startingNode.getIslands()) {                                                       //for loop to move through all the IslandTile in each node
                if (island.getID() == islandID)
                    island.addStudent(student);                                                                         //calls addStudent from IslandTile
            }
        }
    }

    /**
     * method to get motherNature position based by which island has the motherNature flag set on
     * @return a node containing motherNature
     */
    public Node getMotherNature() {
        Node startingNode = this.head;                                                                                  //starting from the head node
        while (startingNode.getNextNode() != head && !startingNode.checkMotherNature()) {                               //iterate through all the list until we find the node with motherNature on
            startingNode = startingNode.getNextNode();
        }
        return startingNode;
    }

    /**
     * method to get the starting point of the islandList
     * @return
     */
    public Node getHead() {
        return this.head;
    }

    /**
     * method used to move motherNature, can be used to randomly select the starting island
     * @param moves number of islands that motherNature will move through
     */
    public void moveMotherNature(int moves) {
        Node head = this.getMotherNature();                                                                             //starting from the head, it will have the motherNature flag on
        head.resetMotherNature();                                                                                       //reset the motherNature flag
        for(int index = 0; index<moves; index++) {                                                                      //move until we reach the desired island
            head = head.getNextNode();
        }
        head.setMotherNature();                                                                                         //set motherNature flag on
    }

    /**
     * method used to get dominant color of a given islandTile, calls the same method inside the Node
     * @param node island of interest
     * @return the dominant color
     */
    public Color getMostInfluence(Node node) {
        Color mostInfluence = this.getMotherNature().getMostInfluence();
        return mostInfluence;
    }

    /**
     * method used to get the Node containing the island Tile with a given islandID
     * @param ID is the identifier for the island Tile
     * @return a Node containing the selected island Tile
     */
    public Node moveToIsland(int ID) throws InvalidParameterException{
        if(ID>12 || ID<1)
            throw new InvalidParameterException();
        Node startingNode = this.head;
        boolean found=false;
        int index=0;
        while (startingNode.getNextNode() != head && !found) {                                                          //iterates through all the nodes
            while(index<startingNode.getIslands().size() && !found){                                                    //iterates through all the islands
                if (startingNode.getIslands().get(index).getID() == ID)
                    found = true;
            }
        }
        return startingNode;                                                                                            //returns the node containing the island that matches the given ID
    }
}

