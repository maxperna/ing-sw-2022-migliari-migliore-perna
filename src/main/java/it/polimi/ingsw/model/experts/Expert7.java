package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Expert7 implements ExpertCard {

    private int cost = 1;
    private final ArrayList<Color> studentsOnCard = new ArrayList<>();

    private final String IMG = "";            //front image of the card


    public Expert7(Game currentGame){
        try{
            studentsOnCard.addAll(currentGame.getPouch().randomDraw(6));
        }
        catch (NotEnoughStudentsException e){
            e.printStackTrace();
        }
    }
    @Override
    public void useCard(Player user,ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoin,IllegalMove {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoin("You cant afford this card");
        }
        else{
            user.addCoin(-this.cost);
            this.cost++;
            if(studentToSwapBoard.size()!=studentToSwapCard.size())
                throw new IllegalMove("Not the same number of student");
            try {
                this.studentsOnCard.addAll(user.getBoard().moveFromEntryRoom(studentToSwapBoard));
                user.getBoard().addStudentsEntryRoom(studentToSwapBoard);
                this.studentsOnCard.removeAll(studentToSwapCard);

            }
            catch (NotOnBoardException | NotEnoughSpace e){
                e.printStackTrace();
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

    public ArrayList<Color> getStudentsOnCard() {
        return studentsOnCard;
    }
}
