package it.polimi.ingsw.model;


import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.CircularLinkedList.Node;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameField{

    private IslandList islands;                                                                                         //arrayList of nodes containing initially a single island each
    private Pouch pouch;
    private ArrayList<CloudTile> cloudsTile;

    private  GameField() {}

    public static GameField newGameField() {
        return new GameField();
    }

    /**
     * method that calls mergeIsland from islandList
     * @param newMergedIsland is the node with motherNature flag
     * @param islandToMerge is the previous or next island that will be merged into newMergeIsland
     */
    public void mergeIsland(Node newMergedIsland, Node islandToMerge){                                                  //method that calls mergeIsland from islandList
        try{
            islands.mergeIslands(newMergedIsland, islandToMerge);
        }
        catch (EndGameException e) {                                                                                    //catches exception if there are 3 islands and end the game
            e.printStackTrace();
        }


    }

    /**
     * method to extract students and refill cloud
     * @param cloudID is the identifier of the cloud that is actually empty
     */
    public void rechargeCloud(int cloudID){                                                                             //method to recharge a specific cloud
        for(CloudTile cloud : cloudsTile){                                                                              //move through cloudsTile
            if(cloud.getTileID() == cloudID){
                try {
                    ArrayList<Color> newStudents = pouch.randomDraw(3);                                     //randomly draws a given number of students and puts them in an arraylist
                    cloud.rechargeCloud(newStudents);                                                                   //moves students to the selected cloud
                } catch (NotEnoughStudentsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * method to move motherNature by a given number of islands
     * @param move is an int that is retrieved from the card number and is the max number of moves allowed
     */
    public void moveMotherNature(int move){                                                                             //method to move motherNature
        Node head = islands.getMotherNature();                                                                          //get actual motherNature position
        islands.getMotherNature().resetMotherNature();                                                                  //reset motherNature flag in actual island
        for(int index = 0; index < move; index++) {                                                                     //move through the islandList till the move number is matched
            head = head.getNextNode();
        }
        head.setMotherNature();                                                                                         //set motherNature flag
    }

    /**
     * method the get the islandList
     * @return the islandList
     */
    public IslandList getIslands() {
        return islands;
    }

    /**
     * method to set the islandList
     * @param islands
     */
    public void setIslands(IslandList islands) {
        this.islands = islands;
    }

    /**
     * method to get the pouch object
     * @return pouch
     */
    public Pouch getPouch() {
        return pouch;
    }

    /**
     * method to set this game's pouch
     * @param pouch
     */
    public void setPouch(Pouch pouch) {
        this.pouch = pouch;
    }

    /**
     * method used to get the cloudTiles
     * @return an arraylist of cloudTiles
     */
    public ArrayList <CloudTile> getCloudsTile() {
        return cloudsTile;
    }

    /**
     * method used to set the cloudTiles
     * @param cloudsTile is an arrayList of cloudTile
     */
    public void setCloudsTile(ArrayList <CloudTile> cloudsTile) {
        this.cloudsTile = cloudsTile;
    }

    /**
     * method used to get the number of actual nodes in the islandList
     * @return an int
     */
    public int getNumberOfIslands() {
        return  islands.islandCounter(islands.getHead());
    }
}
