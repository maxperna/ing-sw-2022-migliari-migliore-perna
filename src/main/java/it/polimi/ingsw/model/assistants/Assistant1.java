package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Assistant1 implements AssistantCard {
    private final ArrayList<Color> studentOnCard = new ArrayList<>();
    private int cost = 1;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;

    public Assistant1(Game currentGame){
        this.currentGame = currentGame;
        try{
            this.studentOnCard.addAll(this.currentGame.getGameField().getPouch().randomDraw(4));
        }
        catch(NotEnoughStudentsException e){
            e.printStackTrace();
        }
    }
    @Override
    public void useCard(Player user) throws NotEnoughCoin{
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoin("You cant afford this card");
        }
        else{
            user.addCoin(-this.cost);
            this.cost++;

        }
    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return 0;
    }
}
