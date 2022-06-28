package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Expert11 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private final Game currentGame;
    private final ArrayList<Color> studentsOnCard;
    private final String description = "Choose one student from this card and place it on your dining room. Then draw a student and place it on this card";
    private final String IMG = "";            //front image of the card
    private int cost = 2;

    public Expert11(Game currentGame) {
        this.currentGame = currentGame;
        this.studentsOnCard = new ArrayList<>();

        try {
            this.studentsOnCard.addAll(currentGame.getPouch().randomDraw(4));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void endEffect() {
        currentGame.setActiveExpertsCard(null);
    }

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

    @Override
    public ExpertID getExpType() {
        return ID;
    }

    @Override
    public String getExpDescription() {
        return description;
    }
}
