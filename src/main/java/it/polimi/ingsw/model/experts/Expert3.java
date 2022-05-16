package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert3 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private final Game currentGame;
    private int cost = 3;
    private final String IMG = "";            //front image of the card

    public Expert3(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, int targetIsland) throws NotEnoughCoins {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoins("You cant afford this card");
        }
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.checkIslandInfluence(targetIsland);
        }
    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public ExpertID getExpType(){
        return ID;
    }
}
