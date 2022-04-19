package it.polimi.ingsw.model;


import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GameField{

    private final IslandList islands;
    private int numberOfIslands;
    private final Pouch pouch;
    private final ArrayList<CloudTile> cloudsTile;

    public  GameField(UUID gameID, int numberOfPlayers) {

        this.pouch = Pouch.getInstance(gameID);

        ArrayList <CloudTile> cloudTileList = new ArrayList <>();
        for (int i = 0; i < numberOfPlayers; i++) {
            cloudTileList.add(new CloudTile());
        }
        this.cloudsTile = cloudTileList;

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

    public void mergeIsland(){

    }

    public void rechargeCloud(){

    }

    public void moveMotherNature(){

    }

    public IslandList getIslands() {
        return islands;
    }


    public Pouch getPouch() {
        return pouch;
    }


    public ArrayList <CloudTile> getCloudsTile() {
        return cloudsTile;
    }

}
