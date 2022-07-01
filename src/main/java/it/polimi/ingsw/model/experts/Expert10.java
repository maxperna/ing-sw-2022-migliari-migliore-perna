package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class implementing assistant card 10 having the following effect: switch up to 2 students between your entrance hall and your dining room
 *
 * @author Massimo
 */
public class Expert10 implements ExpertCard {

    private final ExpertID ID = ExpertID.TWO_LIST_COLOR;
    private final String IMG = "images/Personaggi/CarteTOT_front10.jpg";            //front image of the card
    private final Game currentGame;
    private ArrayList<Color> entryRoom;
    private Map<Color,Integer> diningRoom;
    private final String description = "Switch up to 2 students from your entrance hall and your dining room";
    private int cost = 1;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert10(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert10 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when the dining room is empty
     * @throws NotOnBoardException when a student isn't available
     * @param toDiningRoom is an arraylist of students that will be moved to the dining room
     * @param toEntryHall is an arraylist of students that will be moved to the entry hall
     */
    @Override
    public void useCard(Player user, ArrayList<Color> toDiningRoom, ArrayList<Color> toEntryHall) throws NotEnoughCoins, NotOnBoardException, IllegalMove {

        boolean sameColor = false;
        ArrayList<Color> studentsOnHall = user.getBoard().getEntryRoom();                                               //copy of the entry hall

        if (toEntryHall.size() == 2 && toEntryHall.get(0).equals(toEntryHall.get(1)))
            sameColor = true;


        if (user.getNumOfCoin() < cost) {                                                                                   //checking that the player has the required number of coins
            throw new NotEnoughCoins("You don't have enough coins to use this effect\n");
        } else if (user.getBoard().getDiningRoom().isEmpty())                                                             //checking that there is at least a student inside the dining room
            throw new IllegalMove("There are no students inside the dining room\n");
        else {
            if (sameColor) {                                                                                            //checking that the dining room contains 2 students of the given color if those students share the same color
                if (user.getBoard().getDiningRoom().get(toEntryHall.get(0)) < 2)
                    throw new NotOnBoardException("Some students are not available\n");
            } else {
                for (Color student : toEntryHall) {                                                                      //checking that the dining room contains at least a student of the chosen color
                    if (user.getBoard().getDiningRoom().get(student) == 0)
                        throw new NotOnBoardException("Some students are not available\n");
                }
            }

            for (Color student : toDiningRoom) {                                                                         //checking that the entry hall contains the students we want to move to the dining room
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

            for (Color color : Color.values()) {
                currentGame.checkInfluence(user, color);
            }

            currentGame.coinHandler(user, -this.cost);                                                                   //updating expert cost, player coins and expert activated
            this.cost++;
            currentGame.setActiveExpertsCard(this);
        }
    }

    /**
     * Method used to end the effect activated by this expert card
     */
    @Override
    public void endEffect() {
        currentGame.setActiveExpertsCard(null);
    }

    /**
     * Method used to get this card cost
     * @return the number of coins required
     */
    @Override
    public int getCost() {
        return this.cost;
    }

    /**
     * Method used to get the expert ID
     * @return an enum defining the required parameters to use this card
     */
    @Override
    public ExpertID getExpType() {
        return ID;
    }

    /**
     * Method used to get the expert description
     * @return a string describing the expert effect
     */
    @Override
    public String getExpDescription() {
        return description;
    }

    @Override
    public String getIMG() {
        return IMG;
    }

    @Override
    public void makeGameSnap(Player user){
        this.diningRoom = user.getBoard().getDiningRoom();
        this.entryRoom = user.getBoard().getEntryRoom();
    }

    public ArrayList<Color> getEntryRoom() {
        return entryRoom;
    }

    public Map<Color, Integer> getDiningRoom() {
        return diningRoom;
    }
}
