package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.exceptions.NotEnoughElements;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandTile;

import java.util.ArrayList;
import java.util.Collections;

public class TwoPlayersField implements GameFieldCreator{

    @Override
    public void newField() {

        ArrayList<Color> studentToBePlaced = new ArrayList <>();
        for(int j = 0; j < 2; j++ )
        {
            studentToBePlaced.add(Color.RED);
            studentToBePlaced.add(Color.GREEN);
            studentToBePlaced.add(Color.BLUE);
            studentToBePlaced.add(Color.PINK);
            studentToBePlaced.add(Color.YELLOW);
        }
        Collections.shuffle(studentToBePlaced);

        int noStudentTile = (int) Math.floor(Math.random()*(6)+1);

        IslandList islandList = new IslandList();
        ArrayList<IslandTile>  islandTiles = new ArrayList<>();
        for(int i = 1; i <= Game.maxTile; i++)
        {
            IslandTile tile = new IslandTile(i);

            if((i != noStudentTile) & (i != noStudentTile*2)) {
                try {
                    tile.setStudents(GameFactory.drawFromPool(1, studentToBePlaced));
                } catch (NotEnoughElements e) {
                    e.printStackTrace();
                }
            }

            islandTiles.add(tile);
        }
        islandList.addIslands(islandTiles);

        System.out.println("ciao");
    }
}
