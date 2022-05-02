package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Player;

public class Assistant8 implements AssistantCard{

    private int cost = 2;
    @Override
    public void useCard(Player user) throws NotEnoughCoin {

    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return 0;
    }
}
