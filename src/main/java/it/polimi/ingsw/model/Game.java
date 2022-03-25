package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.BoardCreator;
import it.polimi.ingsw.model.factory.GameFactory;
import it.polimi.ingsw.model.factory.GameFieldCreator;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class Game, every class created as its own unique gameID
 * @author Miglia
 */
public class Game {
    public final static int maxPlayers = 4;
    private final UUID gameID;
    private ArrayList<Player> players;
    private  GameField gameField;
    private GameFactory gameFactory;

    /**
     * Constructor
     */
    public Game() {
        this.gameID = UUID.randomUUID();
    }


    public void startGame(String gameMode) {

        //Crea una nuova lista di giocatori
        this.players = new ArrayList<Player>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        this.gameFactory = new GameFactory();

        //Crea i giocatori, li inserisce nella lista e gli associa una board
        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");
        else {
            switch (gameMode) {
                case "TwoPlayers":
                    //aggiunge i giocatori alla lista di giocatori e associa ad ogni giocatore una Board
                    for (int i = 0; i < 2; i++) {
                        this.players.add(new Player());
                        BoardCreator boardCreator2 = this.gameFactory.createBoard(gameMode);
                        this.players.get(i).setBoard(boardCreator2.newBoard());
                    }
                    System.out.println("numero di giocatori " + players.size());
                    //Crea il GameField
                    GameFieldCreator gameFieldCreator2 = this.gameFactory.createField(gameMode);
                    gameFieldCreator2.newField();
                    break;
                case "ThreePlayers":
                    //aggiunge i giocatori alla lista di giocatori e associa ad ogni giocatore una Board
                    for (int i = 0; i < 3; i++) {
                        this.players.add(new Player());
                        BoardCreator boardCreator3 = this.gameFactory.createBoard(gameMode);
                        this.players.get(i).setBoard(boardCreator3.newBoard());
                    }
                    System.out.println("numero di giocatori " + players.size());
                    //Crea il GameField
                    GameFieldCreator gameFieldCreator3 = this.gameFactory.createField(gameMode);
                    gameFieldCreator3.newField();
                    break;
                case "FourPlayers":
                    //aggiunge i giocatori alla lista di giocatori e associa ad ogni giocatore una Board
                    for (int i = 0; i < 4; i++) {
                        this.players.add(new Player());
                        BoardCreator boardCreator4 = this.gameFactory.createBoard(gameMode);
                        this.players.get(i).setBoard(boardCreator4.newBoard());
                    }
                    System.out.println("numero di giocatori " + players.size());
                    //Crea il GameField
                    GameFieldCreator gameFieldCreator4 = this.gameFactory.createField(gameMode);
                    gameFieldCreator4.newField();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown selector " + gameMode);
            }
        }
    }

    /**
     * Getter
     * @return unique ID
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Getter
     * @return the GameField of the match
     */
    public GameField getGameField() {
        return gameField;
    }

    /**
     * Getter
     * @return GameFactory
     */
    public GameFactory getGameFactory() {
        return gameFactory;
    }

    /**
     * Getter
     * @return The list of players in this match
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
}
