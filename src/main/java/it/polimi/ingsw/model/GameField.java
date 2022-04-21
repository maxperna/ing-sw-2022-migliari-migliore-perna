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

    public  GameField(UUID gameID, int numberOfPlayers) {

        this.pouch = Pouch.getInstance(gameID);

        ArrayList <CloudTile> cloudTileList = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            cloudTileList.add(new CloudTile(i));
        }
        this.cloudTiles = cloudTileList;

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

        IslandList islandList = new IslandList();
        ArrayList <IslandTile> islandTiles = new ArrayList <>();
        for (int i = 1; i <= Game.MAX_TILE; i++) {
            IslandTile tile = new IslandTile(i);

            if ((i != noStudentTile) & (i != noStudentTile * 2)) {
                try {
                    tile.setStudents(GameManager.drawFromPool(1, studentToBePlaced));
                } catch (NotEnoughElements e) {
                    e.printStackTrace();
                }
            }

            islandTiles.add(tile);
        }

        islandList.addIslands(islandTiles);
        this.islands = islandList;

    }

    /**
     * method used to call mergeIsland giving 2 islandID
     * @param newMergedIsland ID of the remaining island
     * @param islandToBeMerged ID of the island that will be merged into the remaining one
     * @throws InvalidParameterException when ID is not in range 1-12
     */
    public void mergeIsland(int newMergedIsland, int islandToBeMerged) throws InvalidParameterException {
        if (newMergedIsland < 1 || newMergedIsland > 12 || islandToBeMerged < 1 || islandToBeMerged > 12)               //checks that the islandID is valid
            throw new InvalidParameterException();
        try {
            islands.mergeIslands(newMergedIsland, islandToBeMerged);
            this.decreaseIslands();
        } catch (EndGameException e) {
            e.printStackTrace();
        }
    }

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
        islands.moveMotherNatureWithGivenMoves(moves);
    }

    /**
     * method that moves motherNature to a specified island
     * @param islandID
     */
    public void moveMotherNatureToAGivenIsland (int islandID) {
        islands.moveMotherNatureToIslandTile(islandID);
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

    public void decreaseIslands() {
        this.numberOfIslands = numberOfIslands-1;
    }

    /**
     * method that returns the Node that contains the islandTile with the given ID
     * @param islandTileID
     * @return
     */
    public Node getIslandNode(int islandTileID) {
        return islands.getIslandNode(islandTileID);
    }

    /**
     * method that returns the islandTile that matches the given ID
     * @param islandTileID
     * @return
     */
    public ArrayList<IslandTile> getArrayListOfIslandTile(int islandTileID) {
        return this.getIslandList().getArrayListOfIslandTile(islandTileID);
    }

    /**
     * method that returns the ArrayList of islandTile inside the node with motherNature on
     * @return
     */
    public ArrayList<IslandTile> getMotherNatureArrayList() {
        return this.getIslandList().getMotherNature().getIslandTiles();
    }

    /**
     * method that returns the Node containing motherNature
     * @return
     */
    public Node getMotherNatureNode() {
        return this.getIslandList().getMotherNature();
    }

}
