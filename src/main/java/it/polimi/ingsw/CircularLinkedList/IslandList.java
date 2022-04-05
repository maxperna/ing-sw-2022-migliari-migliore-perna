package it.polimi.ingsw.CircularLinkedList;

import it.polimi.ingsw.CircularLinkedList.Node;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.IslandTile;

import java.util.ArrayList;

/**
 * @author Alessio Migliore
 * class IslandList, composed of up to 12 objects Node, linked in a doubly circular linked list (DCLL)
 */


public class IslandList {
    Node head;

    /**
     * method used to add all Nodes containing an ArrayList of IslandTiles in an initially doubly linked list, when the list has all 12 Nodes, the last element points to the end, making this list circular
     * @param islands that will be part of the nodes (the node can be seen as a superclass of IslandTile, containing all references and methods to make a DCLL)
     */
    public void addIslands(ArrayList<IslandTile> islands) {
        for (IslandTile island : islands) {
            Node newIsland = new Node(island);
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
    public IslandList(){}


    /**
     * method to merge islands, it transfer the ArrayList IslandTile from the node that we eliminate to the one we call the method on
     * @param newMergedIsland is the island that remains in the linked list
     * @param islandToMerge is the island whose information is going to be moved inside the other island, this one will be cancelled by garbage collector
     * @throws EndGameException when there are exactly 3(?) islands left inside the list
     */
    public void mergeIslands(Node newMergedIsland, Node islandToMerge) throws EndGameException {                        //not "tested"
        newMergedIsland.addIslands(islandToMerge.getIslands());                                                         //instruction to add islands from the node we intend to merge to the final one
        newMergedIsland.setNextNode(islandToMerge.getNextNode());                                                       //new island's next node becomes merged island's next node
        newMergedIsland.getNextNode().setPreviousNode(newMergedIsland);                                                 //merged island's next node stores into previous node the pointer to the new node
        if(this.islandCounter(this.head) == 3)
            throw new EndGameException();
    }

    /**
     * method used to count elements inside a linked list (circular or not)
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
     *method that returns the last node in a list (circular or not)
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
     * @param node is the island (or superisland) we want to know about
     * @return Color containing dominant color
     */
    public Color getInfluence(Node node){                                                                               //method to check influence on an island or on merged islands
        Color maxColor = null;                                                                                          //declare dominant color as null
        int maxStudents = 0;                                                                                            //declare max students of dominant color as 0
        int colorCounter = 0;
        for(Color actualColor : Color.values()){                                                                        //iterates for all colors of students
            for(int island=0; island<node.islands.size(); island++){                                                    //iterates for all islands in the node
                ArrayList<Color> students = node.islands.get(island).getStudents();                                     //creates a local copy of all students in the visited island
                for (Color color : students) {                                                 //iterates for all students in previously declared ArrayList
                    if (color.equals(actualColor))                                                       //checks if the student's color matches with current color
                        colorCounter++;                                                                                 //increases color counter for that color
                }
                if(maxStudents < colorCounter){                                                                         //updates dominant color and number of dominant students values
                    maxStudents = colorCounter;
                    maxColor = actualColor;
                }
            }
        }
        return maxColor;                                                                                                //returns the dominant color in the island
    }
}

