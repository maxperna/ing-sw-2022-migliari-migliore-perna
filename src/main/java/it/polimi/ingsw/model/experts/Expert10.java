package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class Expert10 implements ExpertCard {

    private final ExpertID ID = ExpertID.TWO_LIST_COLOR;
    private int cost = 1;
    private final String IMG = "";            //front image of the card
    private final Game currentGame;

    public Expert10(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, ArrayList<Color> studentInside, ArrayList<Color> studentOutside) throws NotEnoughCoins,IllegalArgumentException {
        if(user.getNumOfCoin()<cost){
            throw new NotEnoughCoins();
        }
        else{
            currentGame.coinHandler(user,this.cost);
            this.cost++;

            try {
                ArrayList<Color> colorOutside = new ArrayList<>(user.getBoard().moveFromEntryRoom(studentOutside));   //removing students from the outside
                ArrayList<Color> colorInside = new ArrayList<>(user.getBoard().moveFromDiningRoom(studentInside));      //removing students from the inside

                user.getBoard().addStudentsEntryRoom(colorInside);
                user.getBoard().addStudentsDiningRoom(colorOutside);
            }
            catch (NotEnoughSpace | NotOnBoardException e){
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

    @Override
    public ExpertID getExpType(){
        return ID;
    }
}
