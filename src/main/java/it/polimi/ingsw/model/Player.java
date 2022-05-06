package it.polimi.ingsw.model;


import java.util.UUID;

public class Player {

    private String nickname;
    private Board board;

    private final UUID playerID;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDeck(CardDeck deck) {
        this.deck = deck;
    }

    public void addCoin(int quantity){
        this.numOfCoin = this.numOfCoin + quantity;
    }

    public TowerColor getTowerColor() {
        return this.board.getTowerColor();
    }

    public int getNumOfCoin(){
        return numOfCoin;
    }

    public UUID getPlayerID() {
        return playerID;
    }
}
