package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * Class implementing assistant card 4 having the following effect: move Mother Nature up to 2 more islands over the limit shown on the action card
 *
 * @author Massimo
 */
public class Expert4 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;
    private final String description = "You can move Mother Nature up to 2 more islands over the limit shown on the action card";
    private int cost = 1;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert4(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     */
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if (user.getNumOfCoin() < cost) throw new NotEnoughCoins("You don't have enough coins to use this effect");
        else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
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
}
