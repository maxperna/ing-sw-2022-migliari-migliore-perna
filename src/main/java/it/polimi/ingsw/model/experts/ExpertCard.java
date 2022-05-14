package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**Interface defying the behavior of an assistant card, every use card method has an ID distinguishing the required
 * parameters
*@author  Massimo
*/
public interface ExpertCard {

    //1
    default void useCard(Player userPlayer) throws NotEnoughCoin{}
    //2
    default void useCard(Player userPlayer, Node targetIsland) throws NotEnoughCoin, IllegalMove {}
    //3
    default void useCard(Player userPlayer,Node targetIsland, Color student) throws NotEnoughCoin,IllegalMove{}
    //4
    default void useCard(Player userPlayer, Color studentToAdd) throws NotEnoughCoin,IllegalMove{}
    //5
    default void useCard(Player user, ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoin, IllegalMove{}

    /**Method to end the effect of the card if that last for an entire round*/
    void endEffect();

    /**Method to get the cost of a card*/
    int getCost();

    /**Method to get the type of parameters required by UseCard method*/
    ExpertID getExpType();
}
