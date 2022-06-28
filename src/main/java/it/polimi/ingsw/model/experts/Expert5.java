package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameField.IsladNode;

import java.util.ArrayList;

public class Expert5 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private final ArrayList<IsladNode> stoppedIsland;
    private final Game currentGame;
    private final String description = "Place a stop card on an island; when Mother Nature reaches that island, skip the check influence phase and remove the stop card.";
    private final String IMG = "";            //front image of the card
    private int stopAvailable;
    private int cost = 2;

    public Expert5(Game currentGame) {
        this.stoppedIsland = new ArrayList<>();
        this.stopAvailable = 4;
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user, int nodeID) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else if (nodeID > currentGame.getGameField().size())
            throw new IllegalMove("This island doesn't exist anymore");
        else if (stopAvailable > 0) {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            IsladNode targetIsland = currentGame.getGameField().getIslandNode(nodeID);
            targetIsland.stopIsland();
            stopAvailable--;
            stoppedIsland.add(targetIsland);
        } else
            throw new IllegalMove("There aren't more deny card");
    }

    @Override
    public void endEffect() {
        //remove the stop on the card
        currentGame.setActiveExpertsCard(null);
    }

    public void removeStop(IsladNode stoppedNode) {
        stoppedIsland.remove(stoppedNode);
        stoppedNode.removeStop();
        this.stopAvailable++;
    }

    /**
     * Method to see the list of current stopped island
     *
     * @return an ArrayList of Nodes
     */
    public ArrayList<IsladNode> getStoppedIsland() {
        return stoppedIsland;
    }

    /**
     * Method to get how many stop remain on the card
     *
     * @return the number of available stop
     */
    public int getStopAvailable() {
        return stopAvailable;
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public ExpertID getExpType() {
        return ID;
    }

    @Override
    public String getExpDescription() {
        return description;
    }
}
