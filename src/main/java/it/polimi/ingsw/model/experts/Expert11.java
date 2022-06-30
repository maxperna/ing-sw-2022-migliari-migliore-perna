package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class implementing assistant card 11 having the following effect: choose a student from this card and move it to your dining room,
 * then draw a student from the pouch and place it on this card
 *
 * @author Massimo
 */
public class Expert11 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private final Game currentGame;
    private final ArrayList<Color> studentsOnCard;
    private final String description = "Choose one student from this card and place it on your dining room. Then draw a student and place it on this card";
    private final String IMG = "images/Personaggi/CarteTOT_front11.jpg";            //front image of the card
    private int cost = 2;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert11(Game currentGame) {
        this.currentGame = currentGame;
        this.studentsOnCard = new ArrayList<>();

        try {
            this.studentsOnCard.addAll(currentGame.getPouch().randomDraw(4));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to activate Expert11 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when there are too many students in the dining room or when the chosen student is not available
     * @param colorToAdd is the student that will be moved to the dining room
     */
    @Override
    public void useCard(Player user, Color colorToAdd) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < this.cost)
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);

            if (!studentsOnCard.contains(colorToAdd)) {
                currentGame.coinHandler(user, this.cost);
                this.cost--;
                throw new IllegalMove("This student is not available");
            } else {
                try {
                    studentsOnCard.remove(colorToAdd);
                    user.getBoard().addStudentsDiningRoom(new ArrayList<>(Collections.singleton(colorToAdd)));
                    studentsOnCard.addAll(currentGame.getPouch().randomDraw(1));
                } catch (NotEnoughSpace | NotEnoughStudentsException e) {
                    currentGame.coinHandler(user, this.cost);
                    this.cost--;
                    throw new IllegalMove("Too many students in the entry hall");
                }
            }
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
     * Method to get which students are on card
     *
     * @return an ArrayList of colors
     */
    public ArrayList<Color> getStudentsOnCard() {
        return this.studentsOnCard;
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
}
