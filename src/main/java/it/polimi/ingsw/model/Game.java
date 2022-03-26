package it.polimi.ingsw.model;
import it.polimi.ingsw.model.factory.*;
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
    private ArrayList<Player> playersList;
    private  GameField gameField;
    private GameFactory gameFactory;

    /**
     * Constructor
     */
    public Game() {
        this.gameID = UUID.randomUUID();
    }


    public void startGame(String gameMode) {

        int numberOfPlayers;
        int numberOfDifferentTowers;

        //Crea una nuova lista di giocatori
        this.playersList = new ArrayList<Player>();

        //Crea la fabbrica che verra utilizzata per generare le altri componenti del gioco
        this.gameFactory = new GameFactory();
        BoardCreator boardCreator;
        GameFieldCreator gameFieldCreator;

        if (gameMode == null || gameMode.isEmpty())
            throw new IllegalArgumentException("No GameMode selected");
        else {
            //setta la factory in base alla modalita di gioco selezionata
            switch (gameMode) {
                case "TwoPlayers":
                    boardCreator = this.gameFactory.createBoard(gameMode);
                    gameFieldCreator = this.gameFactory.createField(gameMode);
                    numberOfPlayers = 2;

                    for(int i = 0; i < numberOfPlayers; i++) {
                        this.playersList.add(new Player());                                          //Aggiunge i players alla lista di giocatori
                        if(i==0)
                        this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.BLACK));      //Crea una board e la assegna al giocatore appena creato
                        if(i==1)
                        this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.WHITE));
                    }

                    for (Player player : playersList) {

                        System.out.println("\n");
                        for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                            System.out.println(player.getBoard().getTowers().get(k).getColor());
                    }

                    break;
                case "ThreePlayers":
                    boardCreator = this.gameFactory.createBoard(gameMode);
                    gameFieldCreator = this.gameFactory.createField(gameMode);
                    numberOfPlayers = 3;

                    for(int i = 0; i < numberOfPlayers; i++) {
                        this.playersList.add(new Player());                                          //Aggiunge i players alla lista di giocatori
                        if(i==0)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.BLACK));      //Crea una board e la assegna al giocatore appena creato
                        if(i==1)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.WHITE));
                        if(i==2)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.GRAY));
                    }

                    for (Player player : playersList) {

                        System.out.println("\n");
                        for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                            System.out.println(player.getBoard().getTowers().get(k).getColor());
                    }


                    break;
                case "FourPlayers":
                    boardCreator = this.gameFactory.createBoard(gameMode);
                    gameFieldCreator = this.gameFactory.createField(gameMode);
                    numberOfPlayers = 4;

                    for(int i = 0; i < numberOfPlayers; i++) {
                        this.playersList.add(new Player());                                          //Aggiunge i players alla lista di giocatori
                        if(i==0)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.BLACK));      //Crea una board e la assegna al giocatore appena creato
                        if(i==1)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.WHITE));
                        if(i==2 || i==3)
                            this.playersList.get(i).setBoard(boardCreator.newBoard(TowerColor.TEAMMATE));
                    }

                    for (Player player : playersList) {

                        System.out.println("\n");
                        for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                            System.out.println(player.getBoard().getTowers().get(k).getColor());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown selector " + gameMode);
            }

            System.out.println("numero di giocatori " + playersList.size());
            //Crea il GameField
            gameFieldCreator.newField();
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
    public ArrayList<Player> getPlayersList() {
        return playersList;
    }
}
