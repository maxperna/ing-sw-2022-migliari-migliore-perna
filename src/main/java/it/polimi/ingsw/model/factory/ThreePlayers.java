package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Pouch;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;
import java.util.UUID;

/**
 * TwoPlayers class, part of the factory method, it contains the logic to create a three player's match
 *
 * @author Miglia
 */
public class ThreePlayers implements PlayerCreator {

    private static final int maxStudentHall = 9;
    private static final int maxTowers = 6;
    private static final int numberOfPlayers = 3;


    /**
     * createPlayers
     *
     * @return an ArrayList of 3 players, each one with a board that contains already Students and Towers
     */
    @Override
    public ArrayList <Player> createPlayers(UUID gameID) {

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList <Player> playersCreated = new ArrayList <>();

        //per ogni giocatore...
        for (int i = 0; i < numberOfPlayers; i++) {

            //...viene creata una Board
            Board board = Board.createBoard(maxStudentHall, maxTowers);
            try {
                board.setStudentsOutside(Pouch.getInstance(gameID).randomDraw(maxStudentHall));
            } catch (NotEnoughStudentsException e) {
                e.printStackTrace();
            }

            //...la lista delle torri di ogni giocatore viene popolata
            ArrayList <TowerColor> listOfTowers = new ArrayList <>();
            for (int j = 0; j < maxTowers; j++) {

                if (i == 0) {
                    listOfTowers.add(TowerColor.BLACK);
                } else if (i == 1) {
                    listOfTowers.add(TowerColor.WHITE);
                } else {
                    listOfTowers.add(TowerColor.GRAY);
                }

            }
            board.setTowers(listOfTowers);

            //Crea il giocatore, gli associa la board appena creata e popolata e poi lo inserce nella lista finale
            Player player = new Player();
            player.setBoard(board);
            playersCreated.add(player);
        }

        return playersCreated;
    }
}