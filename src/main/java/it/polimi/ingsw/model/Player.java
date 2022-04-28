package it.polimi.ingsw.model;


import java.util.UUID;

public class Player {

    private final String nickname = "...";
    private final UUID playerID;
    private Board board;
    private CardDeck deck;
    private int numOfCoin;

    public Player (Board board) {

        this.playerID = UUID.randomUUID();
        this.board = board;
        this.numOfCoin = 1;
    }

    public String getNickname(){
        return this.nickname;
    }


    public void setBoard(Board board){
        this.board = board;
    }


    public Board getBoard(){
        return this.board;
    }


    public void setDeck()
    {

    }

    public void addCoin(int quantity){
        this.numOfCoin = this.numOfCoin + quantity;
    }

    public TowerColor getTowerColor() {
        return this.board.getTowerColor();
    }

}
