package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
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
    private final String description = "Switch up to 2 students from your entrance hall and your dining room";

    public Expert10(Game currentGame){
        this.currentGame = currentGame;
    }
    @Override
    public void useCard(Player user, ArrayList<Color> studentInside, ArrayList<Color> studentOutside) throws NotEnoughCoins, IllegalArgumentException, NotOnBoardException, IndexOutOfBoundsException, IllegalMove {
        if(user.getNumOfCoin()<cost){
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        }
        else if (user.getBoard().getDiningRoom().isEmpty())
            throw new IllegalMove("There are no students inside the dining room");
        else{
            currentGame.coinHandler(user,-this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);

            //Check correctness of the parameters
            if(!user.getBoard().getEntryRoom().containsAll(studentOutside)) {
                currentGame.coinHandler(user,this.cost);
                this.cost--;
                throw new IllegalArgumentException("Some students are not available");
            }

            int i = 0;
            Color previousColor = studentInside.get(i);
            for(Color color: studentInside){
                if(i==1 && color.equals(previousColor))
                    if(user.getBoard().getDiningRoom().get(color)<studentInside.size()) {
                        currentGame.coinHandler(user,this.cost);
                        this.cost--;
                        throw new IllegalArgumentException("There are not enough students inside the dining room");
                    }

                else
                    if(user.getBoard().getDiningRoom().get(color)==0) {
                        currentGame.coinHandler(user,this.cost);
                        this.cost--;
                        throw new IllegalArgumentException("There are no students inside the dining room");
                    }
                i++;
            }

            try {
                ArrayList<Color> colorOutside = new ArrayList<>(user.getBoard().moveFromEntryRoom(studentOutside));   //removing students from the outside
                ArrayList<Color> colorInside = new ArrayList<>(user.getBoard().moveFromDiningRoom(studentInside));      //removing students from the inside

                user.getBoard().addStudentsDiningRoom(colorOutside);
                user.getBoard().addStudentsEntryRoom(colorInside);
            }
            catch (NotEnoughSpace e){
                e.printStackTrace();
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

    @Override
    public ExpertID getExpType(){
        return ID;
    }

    @Override
    public String getExpDescription() {
        return description;
    }
}
