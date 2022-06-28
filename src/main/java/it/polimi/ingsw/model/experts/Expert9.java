package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class Expert9 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;
    private final String description = "Choose a color; during this turn the students of that color are not counted during the check influence phase";
    private int cost = 3;

    public Expert9(Game currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user, Color colorToIgnore) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
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
        return cost;
    }

    @Override
    public ExpertID getExpType() {
        return ID;
    }

    @Override
    public String getExpDescription() {
        return description;
    }
}
