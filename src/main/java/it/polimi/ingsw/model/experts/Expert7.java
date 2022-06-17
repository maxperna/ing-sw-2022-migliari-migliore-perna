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
    private final String description = "Choose up to 3 students from this card; switch them with the same number of students of your choice from your entrance hall";

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
    public void useCard(Player user, ArrayList<Color> studentToSwapCard, ArrayList<Color> studentToSwapBoard) throws NotEnoughCoins,IllegalMove {
        if(user.getNumOfCoin()<this.cost){
            throw new NotEnoughCoins("You don't have enough coins to use this card\n");
        }
        else{
            currentGame.coinHandler(user,-this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            /*if(studentToSwapBoard.size()!=studentToSwapCard.size() || studentToSwapBoard.size()>3)
                throw new IllegalMove("Not the same number of student");
            */try {
                for(Color colorToRM:studentToSwapCard)
                this.studentsOnCard.remove(colorToRM);
                this.studentsOnCard.addAll(user.getBoard().moveFromEntryRoom(studentToSwapBoard));
                user.getBoard().addStudentsEntryRoom(studentToSwapCard);
            }
            catch (NotOnBoardException | NotEnoughSpace e){
                throw new IllegalMove("No students on board");
            }

        }
    }

    @Override
    public void endEffect() {
        currentGame.setActiveExpertsCard(null);
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

    @Override
    public String getExpDescription() {
        return description;
    }
}
