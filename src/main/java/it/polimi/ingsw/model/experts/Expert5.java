package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Expert5 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private int stopAvailable;
    private final ArrayList<Node> stoppedIsland;
    private int cost = 2;
    private final Game currentGame;

    private final String IMG = "";            //front image of the card

    public Expert5(Game currentGame){
        this.stoppedIsland = new ArrayList<>();
        this.stopAvailable = 4;
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, int nodeID) throws NotEnoughCoins, IllegalMove {
        if(user.getNumOfCoin()<cost){
            throw new NotEnoughCoins();
        }
        else if(stopAvailable>0){
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            Node targetIsland = currentGame.getGameField().getIslandNode(nodeID);
            targetIsland.stopIsland();
            stopAvailable--;
            stoppedIsland.add(targetIsland);
        }
        else
            throw new IllegalMove("There aren't more deny card");
    }

    @Override
    public void endEffect() {
        //remove the stop on the card
        currentGame.setActiveExpertsCard(null);
    }

    public void removeStop(Node stoppedNode){
        stoppedIsland.remove(stoppedNode);
        this.stopAvailable++;
    }

    /**Method to see the list of current stopped island
     * @return an ArrayList of Nodes*/
    public ArrayList<Node> getStoppedIsland() {
        return stoppedIsland;
    }

    /**Method to get how many stop remain on the card
     * @return the number of available stop*/
    public int getStopAvailable() {
        return stopAvailable;
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public ExpertID getExpType(){
        return ID;
    }
}
