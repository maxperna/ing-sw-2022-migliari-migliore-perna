package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameField.IslandNode;

import java.util.Map;

/**
 * Class implementing assistant card 5 having the following effect: choose an island and check the influence as if mother nature was there
 *
 * @author Massimo
 */
public class Expert3 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private final Game currentGame;
    private Map<Integer, IslandNode> islandList;
    private final String IMG = "images/Personaggi/CarteTOT_front2.jpg";            //front image of the card
    private final String description = "Choose an island and check the influence as if Mother Nature was there.";
    private int cost = 3;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert3(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @param targetIsland is the chosen island where the student will be set
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when the island is not available
     */
    @Override
    public void useCard(Player user, int targetIsland) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else if (targetIsland > currentGame.getGameField().size() || targetIsland < 1)
            throw new IllegalMove("This island doesn't exist anymore");
        else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            try {
                currentGame.checkIslandInfluence(targetIsland);
            } catch (EndGameException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        currentGame.setActiveExpertsCard(null);
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
