package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Player;

public class Expert8 implements ExpertCard {

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
