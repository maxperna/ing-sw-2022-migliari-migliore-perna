package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameField.IslandNode;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class implementing assistant card 3 having the following effect: place a stop card omn a chosen island
 *
 * @author Massimo
 */
public class Expert5 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private final ArrayList<IslandNode> stoppedIsland;
    private Map<Integer,IslandNode> islandList;
    private final Game currentGame;
    private final String description = "Place a stop card on an island; when Mother Nature reaches that island, skip the check influence phase and remove the stop card.";
    private final String IMG = "images/Personaggi/CarteTOT_front4.jpg";            //front image of the card
    private int stopAvailable;
    private int cost = 2;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert5(Game currentGame) {
        this.stoppedIsland = new ArrayList<>();
        this.stopAvailable = 4;
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @param nodeID is the chosen island where the stop card will be set
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when the island is not available or there are no stop cards available
     */
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
            IslandNode targetIsland = currentGame.getGameField().getIslandNode(nodeID);
            targetIsland.stopIsland();
            stopAvailable--;
            stoppedIsland.add(targetIsland);
        } else
            throw new IllegalMove("There aren't more deny card");
    }

    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        //remove the stop on the card
        currentGame.setActiveExpertsCard(null);
    }

    /**
     * Method used to remove a stop card from the island
     * @param stoppedNode is the island that was previously stopped
     */
    public void removeStop(IslandNode stoppedNode) {
        stoppedIsland.remove(stoppedNode);
        stoppedNode.removeStop();
        this.stopAvailable++;
    }

    /**
     * Method to see the list of current stopped island
     *
     * @return an ArrayList of Nodes
     */
    public ArrayList<IslandNode> getStoppedIsland() {
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

    /**
     * Method used to get this card cost
     * @return the number of coins required
     */
    @Override
    public int getCost() {
        return this.cost;
    }

    /**
     * Method used to get the expert ID
     * @return an enum defining the required parameters to use this card
     */
    @Override
    public ExpertID getExpType() {
        return ID;
    }

    /**
     * Method used to get the expert description
     * @return a string describing the expert effect
     */
    @Override
    public String getExpDescription() {
        return description;
    }
    @Override
    public String getIMG() {
        return IMG;
    }

    @Override
    public void makeGameSnap(Player user){
        this.islandList = currentGame.generateGameFieldMap();
    }

    public Map<Integer, IslandNode> getIslandList() {
        return islandList;
    }
}
