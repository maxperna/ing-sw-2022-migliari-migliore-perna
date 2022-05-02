package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Player;

public class Expert9 implements ExpertCard {

    private int cost = 3;
    private final String IMG = "";            //front image of the card
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
