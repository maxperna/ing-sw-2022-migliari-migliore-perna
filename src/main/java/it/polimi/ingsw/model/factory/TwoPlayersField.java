package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.CircularLinkedList.IslandList;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.GameField;
import it.polimi.ingsw.model.Pouch;

import java.util.ArrayList;
import java.util.UUID;

public class TwoPlayersField implements GameFieldCreator{

    private static final int numOfPlayers = 2;

    @Override
    public GameField newField(UUID gameID) {

        GameField gameField = GameField.newGameField();

        ArrayList<CloudTile> cloudTileList = new ArrayList<>();

        for (int i =0; i < numOfPlayers; i++)
        {
            cloudTileList.add(new CloudTile());
        }

        IslandList islandList = GameFactory.builtIslandList();

        gameField.setIslands(islandList);
        gameField.setPouch(Pouch.getInstance(gameID));
        gameField.setCloudsTile(cloudTileList);

        return gameField;
    }

}
