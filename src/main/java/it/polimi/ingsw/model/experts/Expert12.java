package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Expert12 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private int cost = 3;
    private final Game currentGame;
    private final String IMG = "";            //front image of the card
    private final String description = "Choose a color; everyone this turn has to move to the pouch 3 students of that color from its dining room or entrance hall. If there are less than 3 students of that color, move all the students of that color";

    public Expert12(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user, Color colorToRemove) throws NotEnoughCoins {
        if(user.getNumOfCoin()<cost) throw new NotEnoughCoins("You don't have enough coins to use this effect");

        else{
            currentGame.coinHandler(user,-this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            //Temporary list
            ArrayList<Color> colorToList = new ArrayList<>(Collections.singleton(colorToRemove));        //transform color to list as parameter adjustment
            ArrayList<Color> studentToReinsert = new ArrayList<>();  //student to re-add to pouch

            for(Player player:currentGame.getPlayersList()){
                for(int i=0;i<3;i++){
                    try {
                        studentToReinsert.addAll(player.getBoard().moveFromDiningRoom(colorToList));
                    }
                    catch (NotOnBoardException e){
                        break;
                    }
                }
            }
            currentGame.getPouch().addStudents(studentToReinsert);

        }
    }

    @Override
    public void endEffect() {
        currentGame.setActiveExpertsCard(null);
    }

    @Override
    public int getCost() {
        return cost;
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
