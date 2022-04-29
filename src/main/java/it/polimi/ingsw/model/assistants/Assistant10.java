package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Player;

public class Assistant10 implements AssistantCard{
    @Override
    public void useCard(Player user) throws NotEnoughCoin,IllegalArgumentException {

    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return 0;
    }
}
