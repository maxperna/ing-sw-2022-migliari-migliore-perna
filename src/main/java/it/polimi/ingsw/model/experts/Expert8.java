package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * Class implementing assistant card 8 having the following effect: get 2 extra influence points during the check influence phase
 *
 * @author Massimo
 */
public class Expert8 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card
    private final String description = "During this turn, you get 2 extra influence points during the check influence phase";
    private int cost = 2;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert8(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     */
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost)
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.setPlayerHavingPlusTwo(user);
        }
    }

    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        currentGame.setPlayerHavingPlusTwo(null);
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
}
