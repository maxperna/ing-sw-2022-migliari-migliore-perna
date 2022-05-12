package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert8 implements ExpertCard {

    private int cost = 2;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card

    public Expert8(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user) throws NotEnoughCoin {
        if(user.getNumOfCoin()<this.cost)
            throw new NotEnoughCoin();
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setPlayerHavingPlusTwo(user);
        }
    }

    @Override
    public void endEffect() {
        currentGame.setPlayerHavingPlusTwo(null);
    }

    @Override
    public int getCost() {
        return this.cost;
    }
}
