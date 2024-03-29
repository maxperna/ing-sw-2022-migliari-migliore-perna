package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class implementing assistant card 2 having the following effect: during this turn you can take control of profs even
 * with the same number of student
 *
 * @author Massimo
 */
public class Expert2 implements ExpertCard {

    private final ExpertID ID = ExpertID.USER_ONLY;
    private final String IMG = "images/Personaggi/CarteTOT_front2.jpg";
    private final Game currentGame;
    private final String description = "During this turn you can take control of professors even if you only match the maximum number of students of that color on your board";
    private final ConcurrentHashMap<Color, Player> affectedPlayer;   //hashmap to keep track of the affected players
    private int cost = 2;
    private Player usingPlayer = null;     //variable to track the current user of the card

    /**
     * Default constructor
     * @param currentGame is the game this card is associated to
     */
    public Expert2(Game currentGame) {
        this.currentGame = currentGame;
        this.affectedPlayer = new ConcurrentHashMap<>();
    }

    /**
     * Method used to activate Expert2 effect
     * @param user is the player who activated the effect
     * @throws NotEnoughCoins when the player doesn't have the required number of coins
     */
    @Override
    public void useCard(Player user) throws NotEnoughCoins {
        if (user.getNumOfCoin() < this.cost) {
            throw new NotEnoughCoins("You don't have enough coins to use this effect");
        } else {
            currentGame.coinHandler(user, -this.cost);
            this.cost++;
            this.usingPlayer = user;
            currentGame.setActiveExpertsCard(this);
            //Effect
            HashMap<Color, Game.Pair<Player, Integer>> influenceMap = new HashMap<>(currentGame.getInfluenceMap());
            //For every color if the num of student is the same swap the professor
            for (Color color : influenceMap.keySet()) {
                if (user.getBoard().colorStudent(color) == influenceMap.get(color).getNumOfStudents() && influenceMap.get(color).getPlayer() != null) {
                    //keeping track of the affected player for each color
                    affectedPlayer.put(color, influenceMap.get(color).getPlayer());   //ADJUST NULL POINTER EXCEXPTION
                    //Swap procedure
                    influenceMap.get(color).getPlayer().getBoard().removeTeacher(color);
                    user.getBoard().addTeachers(color);
                }
            }

        }
    }

    /**
     * End the effect of the card at the end of the player turn, re-swapping the previously changed professors
     */
    @Override
    public void endEffect() {
        //Re-swapping the professor at the end of the effect
        for (Color colorToSwap : affectedPlayer.keySet()) {
            Player player = affectedPlayer.get(colorToSwap);
            usingPlayer.getBoard().removeTeacher(colorToSwap);
            player.getBoard().addTeachers(colorToSwap);
            currentGame.setActiveExpertsCard(null);
        }
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
    public void makeGameSnap(Player user){}
}
