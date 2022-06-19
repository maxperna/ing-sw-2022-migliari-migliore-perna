package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert6 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private int cost = 3;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card
    private final String description = "During the check influence phase, the towers are not included";

    public Expert6(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You cant afford this card");
        } else {
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.getGameField().ignoreTower();

        }
    }

    @Override
    public void endEffect() {
        //Resetting all ignore tower to false
        currentGame.getGameField().ignoreTower();
        currentGame.setActiveExpertsCard(null);
        }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public ExpertID getExpType(){
        return ID;
    }

    @Override
    public String getExpDescription() {
        return description;
    }
}
