package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.UUID;

import static it.polimi.ingsw.model.factory.TwoPlayers.generateTwoPlayersBlackBoard;
import static it.polimi.ingsw.model.factory.TwoPlayers.generateTwoPlayersWhiteBoard;

/**
 * TwoPlayers class, part of the factory method, it contains the logic to create a four player's match
 * @author Miglia
 */
public class FourPlayer implements PlayerCreator {

    private static final int maxStudentHall = 7;
    private static final int maxTowers = 8;
    private static final int numberOfPlayers = 4;

    /**
     * createPlayers
     * @return an ArrayList of 4 players, each one with a board that contains already Students and Towers
     */
    @Override
    public ArrayList<Player> createPlayers(UUID gameID) {

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList<Player> playersCreated = new ArrayList<>();

        //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
        Player player1 = new Player();
        player1.setBoard(generateTwoPlayersBlackBoard(gameID));
        playersCreated.add(player1);

        Player player2 = new Player();
        player2.setBoard(generateTwoPlayersWhiteBoard(gameID));
        playersCreated.add(player2);

        //per gli altri due giocatori...
        for (int k = 0; k < numberOfPlayers/2; k++) {

            //...viene creata una Board
            Board board = Board.createBoard(maxStudentHall,maxTowers);
            try {
                board.setStudentsOutside(Pouch.getInstance(gameID).randomDraw(maxStudentHall));
            } catch (NotEnoughStudentsException e) {
                e.printStackTrace();
            }

            //...vengono associate le torri del primo e del secondo giocatore, gia creati
            board.setTowers(playersCreated.get(k).getBoard().getTowers());

            //Crea il giocatore, gli associa la board appena creata, poi lo inserce nella lista finale
            Player player = new Player();
            player.setBoard(board);
            playersCreated.add(player);
        }

        return playersCreated;
    }
}