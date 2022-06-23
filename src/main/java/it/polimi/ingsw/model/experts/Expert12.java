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
    private final String description = "Choose a color; everyone this turn has to move to the pouch 3 students of that color from its dining room. If there are less than 3 students of that color, move all the students of that color";

    public Expert12(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public void useCard(Player user, Color colorToRemove) throws NotEnoughCoins, NotOnBoardException {
        if(user.getNumOfCoin()<cost) throw new NotEnoughCoins("You don't have enough coins to use this effect");

        else{
            //Temporary list
            ArrayList<Color> studentToReinsert = new ArrayList<>();  //student to re-add to pouch

            for(Player player:currentGame.getPlayersList()) {

                if (player.getBoard().getDiningRoom().get(colorToRemove) >= 3) {
                    for (int i = 0; i < 3; i++) {
                        studentToReinsert.add(colorToRemove);
                        player.getBoard().getDiningRoom().remove(colorToRemove);
                    }

                }
                else {
                    for(int i=0;i<=player.getBoard().getDiningRoom().get(colorToRemove);i++) {
                        studentToReinsert.add(colorToRemove);
                        player.getBoard().getDiningRoom().remove(colorToRemove);
                    }
                }
                currentGame.getPouch().addStudents(studentToReinsert);
            }

            currentGame.coinHandler(user,-this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
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
