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
    public void useCard(Player user, ArrayList<Color> toDiningRoom, ArrayList<Color> toEntryHall) throws NotEnoughCoins, IllegalArgumentException, NotOnBoardException, IndexOutOfBoundsException, IllegalMove {

        boolean sameColor = false;
        ArrayList<Color> studentsOnHall = user.getBoard().getEntryRoom();                                               //copy of the entry hall

        if (toEntryHall.size() == 2 && toEntryHall.get(0).equals(toEntryHall.get(1)))
            sameColor = true;


        if(user.getNumOfCoin()<cost){                                                                                   //checking that the player has the required number of coins
            throw new NotEnoughCoins("You don't have enough coins to use this effect\n");
        }
        else if (user.getBoard().getDiningRoom().isEmpty())                                                             //checking that there is at least a student inside the dining room
            throw new IllegalMove("There are no students inside the dining room\n");
        else{
            if (sameColor) {                                                                                            //checking that the dining room contains 2 students of the given color if those students share the same color
                if(user.getBoard().getDiningRoom().get(toEntryHall.get(0)) < 2)
                    throw new NotOnBoardException("Some students are not available\n");
            }
            else {
                for(Color student : toEntryHall) {                                                                      //checking that the dining room contains at least a student of the chosen color
                    if(user.getBoard().getDiningRoom().get(student)==0)
                        throw new NotOnBoardException("Some students are not available\n");
                }
            }

            for(Color student : toDiningRoom) {                                                                         //checking that the entry hall contains the students we want to move to the dining room
                if (!studentsOnHall.remove(student))
                    throw new NotOnBoardException("Some students are not available\n");
            }

            try {
                user.getBoard().addStudentsEntryRoom(toEntryHall);                                                      //adding students to the entry hall
                user.getBoard().moveFromDiningRoom(toEntryHall);                                                        //removing students from the dining room
            } catch (NotEnoughSpace e) {
                e.printStackTrace();
            }

            try {
                user.getBoard().addStudentsDiningRoom(toDiningRoom);                                                    //adding students to the dining room

            } catch (NotEnoughSpace e) {
                e.printStackTrace();
            }

            currentGame.coinHandler(user,-this.cost);                                                                   //updating expert cost, player coins and expert activated
            this.cost++;
            currentGame.setActiveExpertsCard(this);





            /*int i = 0;
            Color previousColor = toDiningRoom.get(i);
            for(Color color: toEntryHall){
                if(i==1 && color.equals(previousColor))
                    if(user.getBoard().getDiningRoom().get(color)<toEntryHall.size()) {
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
                ArrayList<Color> colorOutside = new ArrayList<>(user.getBoard().moveFromEntryRoom(toDiningRoom));   //removing students from the outside
                ArrayList<Color> colorInside = new ArrayList<>(user.getBoard().moveFromDiningRoom(toEntryHall));      //removing students from the inside

                user.getBoard().addStudentsDiningRoom(colorOutside);
                user.getBoard().addStudentsEntryRoom(colorInside);
            }
            catch (NotEnoughSpace e){
                e.printStackTrace();
            }*/
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
