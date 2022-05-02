package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.circularLinkedList.Node;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Assistant3 implements AssistantCard {

    private final Game currentGame;
    private int cost = 3;
    public Assistant3(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, Node targetIsland) throws NotEnoughCoin {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoin("You cant afford this card");
        }
        else{
            this.cost++;
            user.addCoin(-this.cost);
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
}
