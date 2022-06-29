package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**
 * Class implementing assistant card 7 having the following effect: swap up to 3 students from this card with your entry room
 *
 * @author Massimo
 */
public class Expert7 implements ExpertCard {

    private final ExpertID ID = ExpertID.TWO_LIST_COLOR;
    private final ArrayList<Color> studentsOnCard;
    private final Game currentGame;
    private final String description = "Choose up to 3 students from this card; switch them with the same number of students of your choice from your entrance hall";
    private final String IMG = "";            //front image of the card
    private int cost = 1;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert7(Game currentGame) {
        this.currentGame = currentGame;
        this.studentsOnCard = new ArrayList<>();
        try {
            studentsOnCard.addAll(currentGame.getPouch().randomDraw(6));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when one of the students is not available
     * @param studentToSwapBoard is an arraylist of students that will be moved to the card
     * @param studentToSwapCard is an arraylist of students that will be moved to the board
     */
    @Override
    public void useCard(Player user, ArrayList<Color> studentToSwapCard, ArrayList<Color> studentToSwapBoard) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            try {
                for (Color colorToRM : studentToSwapCard)
                    this.studentsOnCard.remove(colorToRM);
                this.studentsOnCard.addAll(user.getBoard().moveFromEntryRoom(studentToSwapBoard));
                user.getBoard().addStudentsEntryRoom(studentToSwapCard);
            } catch (NotOnBoardException | NotEnoughSpace e) {
                this.studentsOnCard.addAll(studentToSwapCard);
                this.studentsOnCard.removeAll(studentToSwapBoard);

                for (Color student : studentToSwapCard)
                    user.getBoard().getEntryRoom().remove(student);

                this.cost--;
                currentGame.coinHandler(user, this.cost);
                throw new IllegalMove("No students on board");
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
        return studentsOnCard;
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
}
