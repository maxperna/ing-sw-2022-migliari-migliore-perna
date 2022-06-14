package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert8 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private int cost = 2;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card
    private final String description = "During this turn, you get 2 extra influence points during the check influence phase";

    public Expert8(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if(user.getNumOfCoin()<this.cost)
            throw new NotEnoughCoins();
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.setPlayerHavingPlusTwo(user);
        }
    }

    @Override
    public void endEffect() {
        currentGame.setPlayerHavingPlusTwo(null);
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
