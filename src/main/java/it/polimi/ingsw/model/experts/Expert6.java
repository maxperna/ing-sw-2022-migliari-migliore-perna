package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert6 implements ExpertCard {

    private int cost = 3;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card

    public Expert6(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user) throws NotEnoughCoin {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoin("You cant afford this card");
        } else {
            this.cost++;
            user.addCoin(-this.cost);
            currentGame.getGameField().ignoreTower();

        }
    }

    @Override
    public void endEffect() {
        //Resetting all ignore tower to false
        currentGame.getGameField().ignoreTower();
        }

    @Override
    public int getCost() {
        return this.cost;
    }
}
