package it.polimi.ingsw.model;


import it.polimi.ingsw.CircularLinkedList.IslandList;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameField{

    private IslandList islands;
    private int numberOfIslands;
    private Pouch pouch;
    private ArrayList<CloudTile> cloudsTile;

    private  GameField() {}

    public static GameField newGameField() {
        return new GameField();
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

    public void setIslands(IslandList islands) {
        this.islands = islands;
    }

    public Pouch getPouch() {
        return pouch;
    }

    public void setPouch(Pouch pouch) {
        this.pouch = pouch;
    }

    public ArrayList <CloudTile> getCloudsTile() {
        return cloudsTile;
    }

    public void setCloudsTile(ArrayList <CloudTile> cloudsTile) {
        this.cloudsTile = cloudsTile;
    }
}
