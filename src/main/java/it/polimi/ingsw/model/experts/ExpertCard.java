package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**Interface defying the behavior of an assistant card
*@author  Massimo
*/
public interface ExpertCard {

    //1
    default void useCard(Player userPlayer) throws NotEnoughCoin{};
    //2
    default void useCard(Player userPlayer, Node targetIsland) throws NotEnoughCoin, IllegalMove {};
    //3
    default void useCard(Player userPlayer,Node targetIsland, Color student) throws NotEnoughCoin,IllegalMove{};
    //4
    default void useCard(Player userPlayer, Color studentToAdd) throws NotEnoughCoin,IllegalMove{};
    //5
    default void useCard(Player user, ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoin, IllegalMove{};

    public void endEffect();

    public int getCost();
}
