package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Expert1 implements ExpertCard {
    private final ArrayList<Color> studentsOnCard = new ArrayList<>();
    private int cost = 1;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;

    public Expert1(Game currentGame){
        this.currentGame = currentGame;
        try{
            this.studentsOnCard.addAll(this.currentGame.getPouch().randomDraw(4));
        }
        catch(NotEnoughStudentsException e){
            e.printStackTrace();
        }
    }
    @Override
    public void useCard(Player user, Node targetIsland) throws NotEnoughCoin{
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoin("You cant afford this card");
        }
        else{
            user.addCoin(-this.cost);
            this.cost++;
            Color colorToSwap = null;     //input, color to move on the island
            //COLOR INPUT
            studentsOnCard.remove(colorToSwap);
            targetIsland.addStudent(colorToSwap);
            try {
                studentsOnCard.addAll(currentGame.getPouch().randomDraw(1));
            }
            catch (NotEnoughStudentsException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return cost;
    }
}
