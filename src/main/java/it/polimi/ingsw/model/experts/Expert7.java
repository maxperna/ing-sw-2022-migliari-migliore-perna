package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Expert7 implements ExpertCard {

    private final ExpertID ID = ExpertID.TWO_LIST_COLOR;
    private int cost = 1;
    private final ArrayList<Color> studentsOnCard;
    private final Game currentGame;

    private final String IMG = "";            //front image of the card


    public Expert7(Game currentGame){
        this.currentGame = currentGame;
        this.studentsOnCard = new ArrayList<>();
        try{
            studentsOnCard.addAll(currentGame.getPouch().randomDraw(6));
        }
        catch (NotEnoughStudentsException e){
            e.printStackTrace();
        }
    }
    @Override
    public void useCard(Player user,ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoins,IllegalMove {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoins("You cant afford this card");
        }
        else{
            currentGame.coinHandler(user,this.cost);
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

    /**Method to get which students are on card
     * @return an ArrayList of colors*/
    public ArrayList<Color> getStudentsOnCard() {
        return studentsOnCard;
    }

    @Override
    public ExpertID getExpType(){
        return ID;
    }
}
