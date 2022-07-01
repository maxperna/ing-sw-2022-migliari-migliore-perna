package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameField.IslandNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class implementing Expert1: move a student from this card to a chosen island
 *
 * @author Massimo
 */
public class Expert1 implements ExpertCard {


    private final ExpertID ID = ExpertID.NODE_ID_COLOR;
    private final ArrayList<Color> studentsOnCard = new ArrayList<>();
    private final String IMG = "images/Personaggi/CarteTOT_front.jpg";            //front image of the card
    private final Game currentGame;
    private Map<Integer,IslandNode> islandMap;
    private final String description = "Choose one of the students on this card and place it on one island, then draw a student from the pouch and place it on this card";
    private int cost = 1;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert1(Game currentGame) {
        this.currentGame = currentGame;
        try {
            this.studentsOnCard.addAll(this.currentGame.getPouch().randomDraw(4));
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to activate Expert1 effect
     * @param user is the player who activated the effect
     * @param nodeID is the island chosen
     * @param colorToSwap is the student that will be moved to the island
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @throws IllegalMove when the student chosen is not available on the card or the island is not available
     */
    @Override
    public void useCard(Player user, int nodeID, Color colorToSwap) throws NotEnoughCoins, IllegalMove {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            currentGame.setActiveExpertsCard(this);

            if(nodeID < 1 || nodeID > currentGame.getGameField().size())
                throw new IllegalMove("Island not available");

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
        return cost;
    }

    /**
     * Method to get which students are on card
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

    @Override
    public void makeGameSnap(Player user) {
        this.islandMap = currentGame.generateGameFieldMap();
    }

    public Map<Integer, IslandNode> getIslandMap() {
        return islandMap;
    }

    @Override
    public String getIMG() {
        return IMG;
    }
}
