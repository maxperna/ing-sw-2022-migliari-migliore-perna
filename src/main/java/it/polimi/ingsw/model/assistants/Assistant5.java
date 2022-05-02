package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.circularLinkedList.Node;
import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Assistant5 implements AssistantCard{
    private int stopAvailable;
    private final ArrayList<Node> stoppedIsland = new ArrayList<>();
    private int cost = 2;

    public Assistant5(){
        this.stopAvailable = 4;
    }
    @Override
    public void useCard(Player user, Node targetIsland) throws NotEnoughCoin, IllegalMove {
        if(user.getNumOfCoin()<cost){
            throw new NotEnoughCoin();
        }
        else if(stopAvailable>0){
            user.addCoin(-this.cost);
            this.cost++;
            targetIsland.stopIsland();
            stopAvailable--;
            stoppedIsland.add(targetIsland);
        }
        else
            throw new IllegalMove("There aren't more deny card");
    }

    @Override
    public void endEffect() {
        //remove the stop on the card
        stopAvailable++;
    }

    @Override
    public int getCost() {
        return this.cost;
    }
}
