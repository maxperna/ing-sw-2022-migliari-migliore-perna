package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * Class implementing assistant card 6 having the following effect: exclude towers during check influence phase
 *
 * @author Massimo
 */
public class Expert6 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private final Game currentGame;
    private final String IMG = "images/Personaggi/CarteTOT_front6.jpg";            //front image of the card
    private final String description = "During the check influence phase, the towers are not included";
    private int cost = 3;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert6(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     */
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.getGameField().ignoreTower();
        }
    }

    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        //Resetting all ignore tower to false
        currentGame.getGameField().ignoreTower();
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
    public void makeGameSnap(Player user){}
}
