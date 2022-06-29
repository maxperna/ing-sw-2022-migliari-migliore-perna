package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * Class implementing assistant card 9 having the following effect: choose a color of students that will not be counted during this turn's check influence phase
 *
 * @author Massimo
 */
public class Expert9 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;
    private final String description = "Choose a color; during this turn the students of that color are not counted during the check influence phase";
    private int cost = 3;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert9(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert9 effect
     * @param user is the player who activated the effect
     * @param colorToIgnore is the color of the students that will be ignored during the check influence
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     */
    @Override
    public void useCard(Player user, Color colorToIgnore) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.setIgnoredColor(colorToIgnore);
        }

    }


    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        currentGame.setIgnoredColor(null);
        currentGame.setActiveExpertsCard(null);
    }

    /**
     * Method used to get this card cost
     * @return the number of coins required
     */
    @Override
    public int getCost() {
        return cost;
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
