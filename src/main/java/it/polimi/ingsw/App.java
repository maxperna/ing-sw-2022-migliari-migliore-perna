package it.polimi.ingsw;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.TowerColor;

import java.io.FileNotFoundException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            Game twoPlayersGame = GameManager.getInstance().initGame("TwoPlayers", false);
            twoPlayersGame.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            twoPlayersGame.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);

            Game threePlayersGame = GameManager.getInstance().initGame("ThreePlayers", false);
            threePlayersGame.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            threePlayersGame.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            threePlayersGame.addPlayer("Pino", DeckType.WITCH, TowerColor.GRAY);

            Game fourPLayersGame = GameManager.getInstance().initGame("FourPlayers", false);
            fourPLayersGame.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            fourPLayersGame.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            fourPLayersGame.addPlayer("Piero2", DeckType.KING, TowerColor.WHITE);
            fourPLayersGame.addPlayer("Gianna2", DeckType.WITCH, TowerColor.BLACK);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int i = 0;
    }
}
