package it.polimi.ingsw.model;


import it.polimi.ingsw.circularLinkedList.IslandList;
import it.polimi.ingsw.circularLinkedList.Node;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughElements;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GameField{

    private final IslandList islands;
    private int numberOfIslands = 12;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudTiles;
    private int coins = 20;    //num of coin

    public  GameField(UUID gameID, int numberOfPlayers) {

        this.pouch = Pouch.getInstance(gameID);

        //creates cloudTiles
        ArrayList <CloudTile> cloudTileList = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            cloudTileList.add(new CloudTile(i));
        }
        this.cloudTiles = cloudTileList;

        //Initialize students on the board
        ArrayList <Color> studentToBePlaced = new ArrayList <>();
        for (int j = 0; j < 2; j++) {
            studentToBePlaced.add(Color.RED);
            studentToBePlaced.add(Color.GREEN);
            studentToBePlaced.add(Color.BLUE);
            studentToBePlaced.add(Color.PINK);
            studentToBePlaced.add(Color.YELLOW);
        }
        Collections.shuffle(studentToBePlaced);

        int noStudentTile = (int) Math.floor(Math.random() * (6) + 1);

        //sets the two island without students with a random number

        ArrayList <Color> students = new ArrayList <>();
        for (int i = 1; i <= Game.MAX_TILE; i++) {
            if ((i != noStudentTile) & (i != noStudentTile +6)) {
                try {
                    students.add(GameManager.drawFromPool(1, studentToBePlaced).get(0));
                } catch (NotEnoughElements e) {
                    e.printStackTrace();
                }
            }
                else
                students.add(Color.BLACK);
            }
        IslandList islandList = new IslandList(students);

        //sets mother nature randomly in one of the two islands without students
        ArrayList<Integer> randomSelection = new ArrayList<>();
        randomSelection.add(noStudentTile);
        randomSelection.add(noStudentTile + 6);
        Collections.shuffle(randomSelection);

        //islandList.addIslands(islandTiles);

        islandList.getIslandNode(randomSelection.get(0)).setMotherNature();

        this.islands = islandList;
    }

    /**
     * method used to call mergeIsland giving 2 islandID
     * @param newMergedIsland ID of the remaining island
     * @param islandToBeMerged ID of the island that will be merged into the remaining one
     * @throws InvalidParameterException when ID is not in range 1-12
     */
    /*public void mergeIsland(int newMergedIsland, int islandToBeMerged) throws InvalidParameterException, EndGameException {
            islands.mergeIslands(newMergedIsland, islandToBeMerged);
            this.decreaseIslands();
    }*/

    /**
     * method used to recharge a cloud given its ID and an arrayList of students
     * @param students will be moved inside the cloud
     * @param cloudID is the ID of the cloudTile that will be recharged
     */
    public void rechargeCloud (ArrayList<Color> students, int cloudID) throws InvalidParameterException{
        if(cloudID<0 || cloudID>=this.cloudTiles.size())
            throw new InvalidParameterException();
        else
            cloudTiles.get(cloudID).rechargeCloud(students);
    }

    /**
     * method that moves MotherNature to an island that is at a certain number of moves apart from the previous position
     * @param moves
     */
    public void moveMotherNatureWithGivenMoves(int moves){
        try {
            islands.moveMotherNatureWithGivenMoves(moves);
        } catch (EndGameException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that moves motherNature to a specified island
     * @param nodeID
     */
    public void moveMotherNatureToNode (int nodeID) throws EndGameException {
        islands.moveMotherNatureToNodeID(nodeID);
    }

    public IslandList getIslandList() {
        return islands;
    }


    public Pouch getPouch() {
        return pouch;
    }


    public ArrayList <CloudTile> getCloudsTile() {
        return cloudTiles;
    }

    private void decreaseIslands() {
        this.numberOfIslands = numberOfIslands-1;
    }

    /**
     * method that returns the Node that contains the islandTile with the given ID
     * @param nodeID
     * @return
     */
    public Node getIslandNode(int nodeID) {
        return islands.getIslandNode(nodeID);
    }

    /**
     * method that returns the ArrayList of students inside the node with motherNature on
     * @return
     */
    public ArrayList<Color> getMotherNatureArrayList() {
        return this.getIslandList().getMotherNature().getStudents();
    }

    /**Method that modify the amount of coin on the gamefield due to a draw by a player
     * @param quantity amount of money to add or remove
     * */
    public void setCoins(int quantity){
        this.coins = this.coins + quantity;
    }

    /**
     * method that returns the Node containing motherNature
     * @return
     */
    public Node getMotherNatureNode() {
        return this.getIslandList().getMotherNature();
    }

    public int getCoins() {
        return coins;
    }

    public boolean isStopped (int islandID) {
        return this.getIslandList().getIslandNode(islandID).isStopped();
    }

    public int getIslandID(int islandID) {
        return this.getIslandList().getIslandNode(islandID).getNodeID();
    }
}
