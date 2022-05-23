package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert9 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private int cost = 3;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;
    public Expert9(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user, Color colorToIgnore) throws NotEnoughCoins {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoins();
        }
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            currentGame.setIgnoredColor(colorToIgnore);
        }

    }

    @Override
    public void endEffect() {
        currentGame.setIgnoredColor(null);
        currentGame.setActiveExpertsCard(null);
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public ExpertID getExpType(){
        return ID;
    }
}
