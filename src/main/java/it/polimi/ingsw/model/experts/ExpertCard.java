package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoins;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**Interface defying the behavior of an assistant card, every use card method has an ID distinguishing the required
 * parameters
*@author  Massimo
*/
public interface ExpertCard {

    //1
    /**Method to use card for experts requiring only the current player (experts 2,4,6,8)
     * @param userPlayer player activating the effect
     * @throws NotEnoughCoins if player hasn't enough coins to activate the card*/
    default void useCard(Player userPlayer) throws NotEnoughCoins {}

    //2
    /**Method to use card for experts requiring user player and a target island (experts 3,5)
     * @param userPlayer player activating the effect
     * @param targetIsland island ID target of the effect
     * @throws NotEnoughCoins if player hasn't enough coins to activate the card
     * @throws IllegalMove if an illegal move is performed*/
    default void useCard(Player userPlayer, int targetIsland) throws NotEnoughCoins, IllegalMove {}

    //3
    /**Method to use card for experts requiring user player and a target island (experts 3,5)
     * @param userPlayer player activating the effect
     * @param targetIsland island ID target of the effect
     * @throws NotEnoughCoins if player hasn't enough coins to activate the card
     * @throws IllegalMove if an illegal move is performed*/
    default void useCard(Player userPlayer,int targetIsland, Color student) throws NotEnoughCoins,IllegalMove{}

    //4
    /**Method to use card for experts requiring user player and a color student (experts 9,11,12)
     * @param userPlayer player activating the effect
     * @param studentToAdd color of the student to pick/add on card/board
     * @throws NotEnoughCoins if player hasn't enough coins to activate the card
     * @throws IllegalMove if an illegal move is performed*/
    default void useCard(Player userPlayer, Color studentToAdd) throws NotEnoughCoins,IllegalMove{}

    //5
    /**Method to use card for experts requiring two set of students (experts 10,7)
     * @param userPlayer player activating the effect
     * @param studentToSwapBoard students to remove from the board
     * @param studentToSwapCard students to remove on the card
     * @throws NotEnoughCoins if player hasn't enough coins to activate the card
     * @throws IllegalMove if an illegal move is performed*/
    default void useCard(Player userPlayer, ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoins, IllegalMove{}

    /**Method to end the effect of the card if that last for an entire round*/
    void endEffect();

    /**Method to get the cost of a card*/
    int getCost();

    /**Method to get the type of parameters required by UseCard method*/
    ExpertID getExpType();

    /**Method used to get the description of the expertCard*/

    String getExpDescription();
}
