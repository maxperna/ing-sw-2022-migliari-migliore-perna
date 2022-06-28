package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameField.IslandNode;

import java.util.ArrayList;

/**
 * Class implementing Expert1:
 *
 * @author Massimo
 */
public class Expert1 implements ExpertCard {


    private final ExpertID ID = ExpertID.NODE_ID_COLOR;
    private final ArrayList<Color> studentsOnCard = new ArrayList<>();
    private final String IMG = "";            //front image of the card
    private final Game currentGame;
    private final String description = "Choose one of the students on this card and place it on one island, then draw a student from the pouch and place it on this card";
    private int cost = 1;

    public Expert1(Game currentGame) {
        this.currentGame = currentGame;
        try {
            this.studentsOnCard.addAll(this.currentGame.getPouch().randomDraw(4));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void useCard(Player user, int nodeID, Color colorToSwap) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);
            if (studentsOnCard.remove(colorToSwap)) {
                IslandNode targetIsland = currentGame.getGameField().getIslandNode(nodeID);
                targetIsland.addStudent(colorToSwap);
                try {
                    studentsOnCard.addAll(currentGame.getPouch().randomDraw(1));
                } catch (NotEnoughStudentsException e) {
                    e.printStackTrace();
                }
            } else {
                currentGame.coinHandler(user, this.cost);
                this.cost--;
                throw new IllegalMove("This student is not available");

            }

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

    /**
     * Method to get which students are on card
     *
     * @return an ArrayList of colors
     */
    public ArrayList<Color> getStudentsOnCard() {
        return studentsOnCard;
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
