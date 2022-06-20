package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert3 implements ExpertCard {

    private final ExpertID ID = ExpertID.NODE_ID;
    private final Game currentGame;
    private int cost = 3;
    private final String IMG = "";            //front image of the card
    private final String description = "Choose an island and check the influence as if Mother Nature was there.";

    public Expert3(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, int targetIsland) throws NotEnoughCoins, IllegalMove {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        }
        else if (targetIsland > currentGame.getGameField().size())
            throw new IllegalMove("This island doesn't exist anymore");
        else{
            currentGame.coinHandler(user,-this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            try{
            currentGame.checkIslandInfluence(targetIsland);
            }catch (EndGameException e ){
                e.printStackTrace();
            }

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

    @Override
    public String getExpDescription() {
        return description;
    }

}
