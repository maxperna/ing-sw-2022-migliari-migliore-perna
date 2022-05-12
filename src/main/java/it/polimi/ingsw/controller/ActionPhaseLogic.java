package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.experts.ExpertCard;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**Class handling the logic of the playing part of the round by a player
 * */
public class ActionPhaseLogic {

    private final Game currentGame;

    public ActionPhaseLogic(Game currentGame){
        this.currentGame = currentGame;
    }
    public void moveStudentOnBoard(Player player,Color color){
        try {
            player.getBoard().moveEntryToDiningRoom(color);
        } catch (NotOnBoardException | NotEnoughSpace e) {
            throw new RuntimeException(e);
        }
    }

    public void moveStudentToIsland(Player player, Color color, int nodeID){
        try {
            player.getBoard().moveToIsland(color,nodeID);
        } catch (NotOnBoardException e) {
            throw new RuntimeException(e);
        }
    }

    /**Method to move mother nature over the island list
     * */
    public void moveMotherNature(Player player,int islandID){
        try {
            currentGame.getGameField().moveMotherNatureToNodeID(islandID);
        }
        catch(InvalidParameterException | EndGameException e){
            e.printStackTrace();
        }
    }

    public void chooseCloudTile(Player player,int cloudID){
        try {
            ArrayList<Color> pickedStudents = new ArrayList<>(currentGame.getCloudTiles().get(cloudID).getStudents());
            player.getBoard().addStudentsEntryRoom(pickedStudents);
        }
        catch (EmptyCloudException | NotEnoughSpace e){
            e.printStackTrace();
        }

    }

    public void playAssistantCard(Player player){

    }

    public void playAssistantCard(Player player,int nodeId){

    }




}
