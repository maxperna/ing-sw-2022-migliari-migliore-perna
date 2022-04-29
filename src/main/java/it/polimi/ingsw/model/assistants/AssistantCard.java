package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.exceptions.IllegalMove;
import it.polimi.ingsw.exceptions.NotEnoughCoin;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**Interface defying the behavior of an assistant card
*@author  Massimo
*/
public interface AssistantCard {

    default void useCard(Player userPlayer) throws NotEnoughCoin{};
    default void useCard(Player userPlayer, ArrayList<Color> studentsToSwap) throws NotEnoughCoin{};

    void useCard(Player user, ArrayList<Color> studentToSwapBoard, ArrayList<Color> studentToSwapCard) throws NotEnoughCoin, IllegalMove;

    public void endEffect();

    public int getCost();
}
