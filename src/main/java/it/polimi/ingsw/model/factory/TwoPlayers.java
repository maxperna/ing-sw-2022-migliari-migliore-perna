package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

/**
 * TwoPlayers class, part of the factory method, it contains the logic to create a two player's match
 * @author Miglia
 */
public class TwoPlayers implements PlayerCreator {

    private static final int maxStudentHall = 7;
    private static final int maxTowers = 8;
    private static final int numberOfPlayers = 2;

    /**
     * createPlayers
     * @return an ArrayList of 2 players, each one with a board that contains already Students and Towers
     */
    @Override
    public ArrayList<Player> createPlayers() {

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList<Player> playersCreated = new ArrayList<>();

        //per ogni giocatore...
        for (int i = 0; i < numberOfPlayers; i++) {

            //...viene creata una Board
            Board board = Board.createBoard(maxStudentHall,maxTowers);
            board.setStudentsOutside(Pouch.getInstance().randomDraw(maxStudentHall));

            //...la lista delle torri di ogni giocatore viene popolata
            ArrayList<Tower> listOfTowers = new ArrayList<>();
            for (int j = 0; j < maxTowers; j++) {

                TowerColor towerColor;

                if(i == 0) {
                    towerColor = TowerColor.BLACK;
                }
                else {
                    towerColor = TowerColor.WHITE;
                }

                listOfTowers.add(new Tower(towerColor));
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
