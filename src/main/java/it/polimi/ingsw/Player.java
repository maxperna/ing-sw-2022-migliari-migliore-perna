package it.polimi.ingsw;


import it.polimi.ingsw.components.Board;
import it.polimi.ingsw.components.CardDeck;

public class Player {

    private final String nickname;
    private Board board;
    private CardDeck deck;

    public String getNickname(){
        return this.nickname;
    }


    public void setBoard(){

    }


    public Board getBoard(){
        return this.board;
    }


    public void setDeck()
    {

    }


    public Player(String nickname){
        this.nickname=nickname;
    }
}
