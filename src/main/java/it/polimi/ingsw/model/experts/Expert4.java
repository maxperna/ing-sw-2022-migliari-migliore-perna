package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert4 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private int cost = 1;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;

    public Expert4(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if(user.getNumOfCoin()<cost) throw new NotEnoughCoins();
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
        }
    }

    @Override
    public void endEffect() {
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
}
