package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Expert11 implements ExpertCard {

    private int cost = 2;
    private final Game currentGame;
    private final ArrayList<Color> studentsOnCard = new ArrayList<>();

    private final String IMG = "";            //front image of the card

    public Expert11(Game currentGame){
        this.currentGame = currentGame;

        try {
            this.studentsOnCard.addAll(currentGame.getPouch().randomDraw(4));
        }
        catch(NotEnoughStudentsException e){
            e.printStackTrace();
        }
    }
    @Override
    public void useCard(Player user,Color colorToAdd) throws NotEnoughCoin, IllegalMove {
        if(user.getNumOfCoin()<this.cost)
            throw new NotEnoughCoin();
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;

            if (!studentsOnCard.contains(colorToAdd)) throw new IllegalMove("Student is not on card");
            else {
                try {
                    studentsOnCard.remove(colorToAdd);
                    user.getBoard().addStudentsDiningRoom(new ArrayList<>(Collections.singleton(colorToAdd)));
                    studentsOnCard.addAll(currentGame.getPouch().randomDraw(1));
                }
                catch(NotEnoughSpace | NotEnoughStudentsException e){
                e.printStackTrace();
                }
        }

        }
    }

    @Override
    public void endEffect() {

    }

    @Override
    public int getCost() {
        return this.cost;
    }

    public ArrayList<Color> getStudentsOnCard(){
        return this.studentsOnCard;
    }
}
