package it.polimi.ingsw.model;


import it.polimi.ingsw.model.*;

import java.util.UUID;

public class Player {

    private final String nickname = "...";
    private final UUID playerID;
    private Board board;
    private CardDeck deck;

    public Player (Board board) {

        this.playerID = UUID.randomUUID();
        this.board = board;
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

}
