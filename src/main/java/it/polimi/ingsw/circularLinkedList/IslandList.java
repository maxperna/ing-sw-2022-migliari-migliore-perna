package it.polimi.ingsw.circularLinkedList;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.exceptions.StoppedIslandException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Alessio Migliore
 * class IslandList, composed of up to 12 objects Node, linked in a doubly circular linked list (DCLL)
 */


public class IslandList {
    private Node head;

    /**
     * constructor
     */
    public IslandList() {

        //random number for noStudentTile
        int noStudentTile = (int) Math.floor(Math.random() * (6) + 1);

        //array of students for initialising the game
        ArrayList <Color> studentToBePlaced = new ArrayList <>();
        for (int j = 0; j < 2; j++) {
            studentToBePlaced.add(Color.RED);
            studentToBePlaced.add(Color.GREEN);
            studentToBePlaced.add(Color.BLUE);
            studentToBePlaced.add(Color.PINK);
            studentToBePlaced.add(Color.YELLOW);
        }

        //Creates the list with dim elements
        for(int i = 1; i <= Game.MAX_TILE; i++) {

            addNote(i);

            if ((i != noStudentTile) & (i != noStudentTile + 6)) {
                try {
                    getIslandNode(i).getStudents().addAll(GameManager.drawFromPool(1,studentToBePlaced));
                } catch (NotEnoughElements e) {
                    e.printStackTrace();
                }
            }
        }

        //sets mother nature randomly in one of the two islands without students
        ArrayList<Integer> randomSelection = new ArrayList<>();
        randomSelection.add(noStudentTile);
        randomSelection.add(noStudentTile + 6);
        Collections.shuffle(randomSelection);
        this.getIslandNode(randomSelection.get(0)).setMotherNature();
    }


    /**
     * method to merge islands, it transfers the ArrayList IslandTile from the node that we eliminate to the one we call the method on
     *
     * @param newMergedIsland is the island that remains in the linked list
     * @param islandToMerge   is the island whose information is going to be moved inside the other island, this one will be cancelled by garbage collector
     * @throws EndGameException when there are exactly 3(?) islands left inside the list
     */
    public void mergeIslands (int newMergedIsland, int islandToMerge) throws EndGameException, InvalidParameterException {
        Node newIsland = this.getIslandNode(newMergedIsland);
        Node oldIsland = this.getIslandNode(islandToMerge);
        if(this.islandCounter() == 3)
            throw new EndGameException();
        if(islandToMerge == 1) {
            newIsland = this.getIslandNode(islandToMerge);
            oldIsland = this.getIslandNode(newMergedIsland);
            newIsland.mergeStudents(oldIsland.getStudents());
            newIsland.setPreviousNode(oldIsland.getPreviousNode());
        }
        else {
            if(!(this.getIslandNode(newMergedIsland).getNextNode().equals(this.getIslandNode(islandToMerge)) || this.getIslandNode(newMergedIsland).getPreviousNode().equals(this.getIslandNode(islandToMerge))))
                throw new InvalidParameterException();
            newIsland.mergeStudents(oldIsland.getStudents());                                                               //instruction to add islands from the node we intend to merge to the final one
            newIsland.setNextNode(oldIsland.getNextNode());                                                                 //new island's next node becomes merged island's next node
            newIsland.getNextNode().setPreviousNode(newIsland);                                                             //merged island's next node stores into previous node the pointer to the new node
            if(newIsland.getNodeID()>oldIsland.getNodeID())
                newIsland.decreaseNodeID();

            if (this.islandCounter()<=3)
                throw new EndGameException();

            Node currIsland = newIsland;
            while(currIsland.getNextNode().getNodeID()!=1) {
                currIsland.getNextNode().decreaseNodeID();
                currIsland = currIsland.getNextNode();

            }
        }
        if(newIsland.getTowerColor().equals(newIsland.getNextNode().getTowerColor())) {
            if(newIsland.getNodeID()>newIsland.getNextNode().getNodeID())
                mergeIslands(newIsland.getNextNode().getNodeID(), newIsland.getNodeID());
            else
                mergeIslands(newIsland.getNodeID(), newIsland.getNextNode().getNodeID());
        }
        else if(newIsland.getTowerColor().equals(newIsland.getPreviousNode().getTowerColor())) {
            if(newIsland.getNodeID()>newIsland.getPreviousNode().getNodeID())
                mergeIslands(newIsland.getPreviousNode().getNodeID(), newIsland.getNodeID());
            else
                mergeIslands(newIsland.getNodeID(), newIsland.getPreviousNode().getNodeID());
        }
    }

    /**
     * method used to count elements inside a linked list (circular or not)
     * @return counter, an int that contains the number of nodes inside the list
     */
    public int islandCounter() {
        int counter = 0;                                                                                                //counter variable to count the number of nodes inside the linked list
        Node startingNode = this.getHeadNode();                                                                         //new node to move through the linked list

        do {                                                                                                            //cycle to move through the linked list, used to check number of nodes inside the list with counter variable
            counter++;
            startingNode = startingNode.getNextNode();
        }while (startingNode.getNextNode() != null && !startingNode.equals(head));
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
     * method to add a student to a given island
     * @param nodeID (0-11), identifies the island
     * @param student  to be added
     * @throws InvalidParameterException when islandID is invalid
     */
    public void addStudent(int nodeID, Color student) throws InvalidParameterException {                              //method that adds a single student to a specific IslandTIle
        if (nodeID < 1 || nodeID > this.islandCounter())                                                                              //checks that the islandID is valid
            throw new InvalidParameterException();
        Node actualNode = this.getIslandNode(nodeID);
        actualNode.addStudent(student);
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
    public Node getHeadNode() {
        return this.head;
    }

    /**
     * method used to move motherNature, can be used to randomly select the starting island
     * @param moves number of islands that motherNature will move through
     */
    public void moveMotherNatureWithGivenMoves(int moves) throws EndGameException, InvalidParameterException {
        Node actualNode = this.getMotherNature();                                                                       //starting from the head, it will have the motherNature flag on
        actualNode.resetMotherNature();                                                                                 //reset the motherNature flag
        for(int index = 0; index<moves; index++) {                                                                      //move until we reach the desired island
            actualNode = actualNode.getNextNode();
        }
        actualNode.setMotherNature();                                                                                   //set motherNature flag on
        actualNode.setTower(actualNode.getMostInfluencePlayer());
        if(actualNode.getTowerColor().equals(actualNode.getNextNode().getTowerColor())) {
            if(actualNode.getNodeID()>actualNode.getNextNode().getNodeID())
                mergeIslands(actualNode.getNextNode().getNodeID(), actualNode.getNodeID());
            else
                mergeIslands(actualNode.getNodeID(), actualNode.getNextNode().getNodeID());
        }
        else if(actualNode.getTowerColor().equals(actualNode.getPreviousNode().getTowerColor())) {
            if(actualNode.getNodeID()>actualNode.getPreviousNode().getNodeID())
                mergeIslands(actualNode.getPreviousNode().getNodeID(), actualNode.getNodeID());
            else
                mergeIslands(actualNode.getNodeID(), actualNode.getPreviousNode().getNodeID());
        }
    }

    /**
     * method used to get dominant color of a given islandTile, calls the same method inside the Node
     * @param node island of interest
     * @return the dominant color
     */
    public Color getMostInfluence(Node node) {
        Color mostInfluence = null;
        try {
            mostInfluence = node.getMostInfluence();
        } catch (StoppedIslandException e) {
            e.printStackTrace();
        }
        return mostInfluence;
    }

    /**
     * method used to set motherNature flag of the node that contains the islandTile that matches the given ID
     * @param nodeID is the identifier for the island Tile
     */
    public void moveMotherNatureToNodeID(int nodeID) throws InvalidParameterException, EndGameException{
        if(nodeID>this.islandCounter() || nodeID<1)
            throw new InvalidParameterException();
        Node actualNode = this.getMotherNature();
        actualNode.resetMotherNature();
        actualNode = this.getIslandNode(nodeID);
        actualNode.setMotherNature();                                                                                   //set motherNature flag on
        actualNode.setTower(actualNode.getMostInfluencePlayer());
        if(this.getIslandNode(nodeID).getTowerColor().equals(this.getIslandNode(nodeID).getNextNode().getTowerColor()))
                mergeIslands(this.getIslandNode(nodeID).getNodeID(), this.getIslandNode(nodeID).getNextNode().getNodeID());

        else if(this.getIslandNode(nodeID).getTowerColor().equals(this.getIslandNode(nodeID).getPreviousNode().getTowerColor())) {
                mergeIslands(this.getIslandNode(nodeID).getPreviousNode().getNodeID(), this.getIslandNode(nodeID).getNodeID());
        }

    }

    /**
     * method used to get the node based on the given ID
     * @param nodeID is the ID of the node
     * @return the node with given ID
     * @throws InvalidParameterException when ID in out of range 1-12
     */
    public Node getIslandNode(int nodeID) {

        Node actualNode = this.getHeadNode();

        while(actualNode.getNodeID() != nodeID) {
            actualNode = actualNode.getNextNode();
        }
                                                                                                                        //returns the node containing the island that matches the given ID
        return actualNode;
    }

    /**
     * method used to get the arrayList of students inside the node that matches the given ID
     * @param nodeID
     * @return  the ArrayList containing the given islandID
     */
    public ArrayList<Color> getStudentsFromIslandNode(int nodeID) {

        Node actualNode = this.getHeadNode();

        while(actualNode.getNodeID() != nodeID) {
            actualNode = actualNode.getNextNode();
        }
        //returns the node containing the island that matches the given ID
        return actualNode.getStudents();
    }

    private void addNote(int nodeID) {

        Node tail;
        Node newNode = new Node(nodeID);

        if (head == null) {
            head = newNode;
            head.setNextNode(head);
            head.setPreviousNode(head);
        }
        else {
            tail = head.getPreviousNode(); //always the last node

            newNode.setPreviousNode(tail);
            newNode.setNextNode(head);

            head.setPreviousNode(newNode);
            tail.setNextNode(newNode);
        }

    }
}



