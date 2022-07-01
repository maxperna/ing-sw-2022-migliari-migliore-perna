package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**
 * Class implementing assistant card 12 having the following effect: choose a color, everyone has to move up to 3 students of that color from its dining room to the pouch
 *
 * @author Massimo
 */
public class Expert12 implements ExpertCard {

    private final ExpertID ID = ExpertID.COLOR;
    private final Game currentGame;
    private final String IMG = "images/Personaggi/CarteTOT_front11.jpg";            //front image of the card
    private final String description = "Choose a color; everyone this turn has to move to the pouch 3 students of that color from its dining room. If there are less than 3 students of that color, move all the students of that color";
    private int cost = 3;

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert12(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Method used to activate Expert12 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     * @param colorToRemove is the color of the students that will be removed from each dining room
     */
    @Override
    public void useCard(Player user, Color colorToRemove) throws NotEnoughCoins {
        if (user.getNumOfCoin() < cost) throw new NotEnoughCoins("You don't have enough coins to use this effect");

        else {
            //Temporary list
            ArrayList<Color> studentToReinsert = new ArrayList<>();  //student to re-add to pouch

            for (Player player : currentGame.getPlayersList()) {

                if (player.getBoard().getDiningRoom().get(colorToRemove) > 3) {
                    for (int i = 0; i < 3; i++) {
                        studentToReinsert.add(colorToRemove);
                        player.getBoard().getDiningRoom().remove(colorToRemove);
                    }
                } else if (player.getBoard().getDiningRoom().get(colorToRemove) == 3) {
                    studentToReinsert.add(colorToRemove);
                    player.getBoard().getDiningRoom().remove(colorToRemove);
                    player.getBoard().getDiningRoom().put(colorToRemove, 0);
                } else {
                    for (int i = 0; i <= player.getBoard().getDiningRoom().get(colorToRemove); i++) {
                        studentToReinsert.add(colorToRemove);
                        player.getBoard().getDiningRoom().remove(colorToRemove);
                        player.getBoard().getDiningRoom().put(colorToRemove, 0);
                    }
                }
                currentGame.getPouch().addStudents(studentToReinsert);
            }


            currentGame.checkInfluence(user, colorToRemove);
            currentGame.coinHandler(user, -this.cost);
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
        return cost;
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
    public void makeGameSnap(Player user){}
}
