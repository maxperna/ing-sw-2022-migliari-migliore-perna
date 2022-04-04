package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Pouch;
import it.polimi.ingsw.model.TowerColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

/**
 * TwoPlayers class, part of the factory method, it contains the logic to create a two player's match
 *
 * @author Miglia
 */
public class TwoPlayers implements PlayerCreator {

    static final int maxStudentHall = 7;
    static final int maxTowers = 8;
    static final int numberOfPlayers = 2;

    public static @NotNull Board generateTwoPlayersBlackBoard(UUID gameID) {
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
            listOfTowers.add(TowerColor.BLACK);
        }
        board.setTowers(listOfTowers);

        return board;
    }

    public static @NotNull Board generateTwoPlayersWhiteBoard(UUID gameID) {
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
            listOfTowers.add(TowerColor.WHITE);
        }
        board.setTowers(listOfTowers);

        return board;
    }

    /**
     * createPlayers
     *
     * @return an ArrayList of 2 players, each one with a board that contains already Students and Towers
     */
    @Override
    public ArrayList <Player> createPlayers(UUID gameID) {

        //Crea un nuovo array di giocatori che verra popolato e poi restituito
        ArrayList <Player> playersCreated = new ArrayList <>();

        //Crea ogni giocatore, gli associa una board popolata e poi lo inserce nella lista finale
        Player player1 = new Player();
        player1.setBoard(generateTwoPlayersBlackBoard(gameID));
        playersCreated.add(player1);

        Player player2 = new Player();
        player2.setBoard(generateTwoPlayersWhiteBoard(gameID));
        playersCreated.add(player2);

        return playersCreated;
    }
}
