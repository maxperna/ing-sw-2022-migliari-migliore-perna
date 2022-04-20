package it.polimi.ingsw.model;


import it.polimi.ingsw.circularLinkedList.IslandList;
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

        //choose a random number between the two possibilities
        ArrayList<Integer> randomFromSet = new ArrayList<>();
        randomFromSet.add(noStudentTile);
        randomFromSet.add(noStudentTile * 2);
        Collections.shuffle(randomFromSet);

        islandList.getIsland(randomFromSet.get(0)).setMotherNature();

        this.islands = islandList;

    }

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

    public void rechargeCloud(ArrayList<Color> students, int ID){
        cloudTiles.get(ID).rechargeCloud(students);
    }

    public void moveMotherNature(int moves){
        islands.moveMotherNature(moves);
    }

    public IslandList getIslands() {
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

    public void moveToIsland(int ID) {
        islands.getIsland(ID);
    }

}
