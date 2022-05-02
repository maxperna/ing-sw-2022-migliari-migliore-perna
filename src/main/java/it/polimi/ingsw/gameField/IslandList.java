package it.polimi.ingsw.gameField;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.TowerColor;

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
        int noStudentTile = (int) Math.floor(Math.random() * (6) + 1);                                                  //randomly chooses two islands with an ID difference of 6

        //array of students for initialising the islands
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

            addNode(i);

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
     * method to merge islands, it transfers the students to the final node
     * @param newMergedIsland is the island that remains in the linked list
     * @param islandToMerge   is the island whose information is going to be moved inside the other island, this one will be cancelled by garbage collector
     * @throws EndGameException when there are exactly 3 islands left inside the list
     */
    private void merge (int newMergedIsland, int islandToMerge) throws EndGameException, InvalidParameterException {    //limit condition, works when the method has to merge the first and last node of the list
        if ((newMergedIsland == 1 && islandToMerge == this.lastNode(head).getNodeID())) {
            head.setMotherNature();                                                                                     //for simplicity, motherNature will be set on island 1, so no nodeID changes for the rest of the list
            head.mergeStudents(this.lastNode(head).getStudents());                                                      //move students from the island that will be deleted to the one that will remain
            head.mergeTowers(this.lastNode(head).getNumberOfTowers());                                                  //basically increases the number of towers on the node (can be referred as nodeDimension too)
            head.setPreviousNode(this.lastNode(head).getPreviousNode());                                                //setting the previous pointer of the head node to skip the last node (i.e. island 1 won't point to 12 but to 11)
            this.lastNode(head).getPreviousNode().setNextNode(head);                                                    //setting the next pointer of the new last node in the list to point to the head (i.e. island 11 won't point to 12 but to 1)
            if (this.islandCounter() == 3)                                                                              //checks for EndGame conditions
                throw new EndGameException();
            mergeIslands(newMergedIsland);                                                                              //calls the method recursively with the remaining island as the parameter to check if there are consequential merges available
        }
        else {                                                                                                          //normal condition, works when we call merge on i, i+1, i different from 1, lastNode
            this.getIslandNode(newMergedIsland).mergeStudents(this.getIslandNode(islandToMerge).getStudents());         //move students from the island that will be deleted to the one that will remain
            this.getIslandNode(newMergedIsland).mergeTowers(this.getIslandNode(islandToMerge).getNumberOfTowers());     //basically increases the number of towers on the node (can be referred as nodeDimension too)
            this.getIslandNode(newMergedIsland).setMotherNature();                                                      //for simplicity, motherNature will be set on the island with the minor ID number
            this.getIslandNode(islandToMerge).getNextNode().setPreviousNode(this.getIslandNode(newMergedIsland));       //setting the previous pointer of the next node pointed by the deleted node to point to the island that will remain (i.e. island 3 won't point to island 2 but to island 1)
            this.getIslandNode(newMergedIsland).setNextNode(this.getIslandNode(islandToMerge).getNextNode());           //setting the next pointer of the remaining node to skip the deleted node (i.e. node 2 won't point to node 3 but to node 4)
            Node currNode = this.getIslandNode(newMergedIsland);                                                        //declaring a node used to iterate through the islandList to decrease the nodeID
            while (currNode.getNextNode().getNodeID() != 1) {                                                           //moves until it reaches the island 1
                currNode.getNextNode().decreaseNodeID();                                                                //decrease the nodeID
                currNode = currNode.getNextNode();
            }
            if (this.islandCounter() == 3)                                                                              //checks for EndGame conditions
                throw new EndGameException();
            mergeIslands(newMergedIsland);                                                                              //calls the method recursively with the remaining island as the parameter to check if there are consequential merges available
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
    public void addStudent(int nodeID, Color student) throws InvalidParameterException {                                //method that adds a single student to a specific IslandTIle
        if (nodeID < 1 || nodeID > this.islandCounter())                                                                //checks that the islandID is valid
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
        actualNode.setTower();                                                                                          //automatically set a tower if there is a player with the most influence on the island
        mergeIslands(actualNode.getNodeID());                                                                           //call mergeIslands
    }

    /**
     * method used to set motherNature flag of the node that contains the islandTile that matches the given ID
     * @param nodeID is the identifier for the island Tile
     * @return a Node containing the selected island Tile
     */
    public void moveMotherNatureToNodeID(int nodeID) throws InvalidParameterException, EndGameException{
        if(nodeID>this.islandCounter() || nodeID<1)
            throw new InvalidParameterException();
        Node actualNode = this.getMotherNature();
        actualNode.resetMotherNature();
        actualNode = this.getIslandNode(nodeID);
        actualNode.setMotherNature();                                                                                   //set motherNature flag on
        actualNode.setTower();
        mergeIslands(nodeID);                                                                                           //call mergeIslands
    }

    /**
     * method used to get the node based on the given ID
     * @param nodeID is the ID of the node
     * @return the node with given ID
     * @throws InvalidParameterException when ID in out of range 1-12
     */
    public Node getIslandNode(int nodeID) throws InvalidParameterException{
        if(nodeID < 1 || nodeID > this.islandCounter()) {
            throw new InvalidParameterException();
        }

        Node actualNode = this.getHeadNode();

        while(actualNode.getNodeID() != nodeID) {
            actualNode = actualNode.getNextNode();
        }
        return actualNode;                                                                                              //returns the node containing the island that matches the given ID
    }

    private void addNode(int nodeID) {

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


    /**
     * method used to check towerColor on next and previous islands of a declared one, if one of them matches with islandID towerColor, calls merge()
     * @param islandID
     * @throws EndGameException from merge()
     */
    public void mergeIslands(int islandID) throws EndGameException {
        if(this.getIslandNode(islandID).getTowerColor().equals(this.getIslandNode(islandID).getNextNode().getTowerColor()) && !this.getIslandNode(islandID).getTowerColor().equals(TowerColor.EMPTY)) {                                             //checks if the next node has the same towerColor of a given node, except EMPTY
            if(islandID > this.getIslandNode(islandID).getNextNode().getNodeID()) {                                                                                                                                                                 //checks which island has the larger ID, so that it calls the method with the correct order of parameters to avoid large and redundant merge() method
                this.getIslandNode(islandID).getNextNode().setMotherNature();                                                                                                                                                                       //setting motherNature on the island with the smaller ID
                merge(this.getIslandNode(islandID).getNextNode().getNodeID(), islandID);                                                                                                                                                            //calls the merge() method if the given island has a match with the next one and the next one is the head
            }
            else
                merge(islandID, this.getIslandNode(islandID).getNextNode().getNodeID());                                                                                                                                                            //calls the merge() method if the given island has a match with the next one and the next one isn't the head
        }
        if(islandID>this.islandCounter())                                                                                                                                                                                                           //since the condition is checked possibly after a merge, if the method was called on the last node, its ID has to be decreased to match the fact that there is one less island on the list
            islandID = this.islandCounter();
        if(this.getIslandNode(islandID).getTowerColor().equals(this.getIslandNode(islandID).getPreviousNode().getTowerColor()) && !this.getIslandNode(islandID).getTowerColor().equals(TowerColor.EMPTY)) {                                         //checks if the previous node has the same towerColor of a given node, except EMPTY
            if(islandID > this.getIslandNode(islandID).getPreviousNode().getNodeID()) {                                                                                                                                                             //checks which island has the larger ID, so that it calls the method with the correct order of parameters to avoid large and redundant merge() method
                this.getIslandNode(islandID).getPreviousNode().setMotherNature();                                                                                                                                                                   //setting motherNature on the island with the smaller ID
                merge(this.getIslandNode(islandID).getPreviousNode().getNodeID(), islandID);                                                                                                                                                        //calls the merge() method if the given island has a match with the previous one and the previous one isn't the tail
            }
            else
                merge(islandID, this.getIslandNode(islandID).getPreviousNode().getNodeID());                                                                                                                                                        //calls the merge() method if the given island has a match with the previous one and the previous one is the tail
        }
    }

    public void ignoreTower(){
        Node nextNode = head;
        while(nextNode.getNextNode()!=head){
            nextNode.changeIgnoreTower();
            nextNode = nextNode.getNextNode();
        }
    }
}



