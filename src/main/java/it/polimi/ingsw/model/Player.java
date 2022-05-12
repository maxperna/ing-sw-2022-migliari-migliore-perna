package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;

import java.io.FileNotFoundException;

public class Player {

    private final String nickname;
    private final Board board;
    private final CardDeck deck;
    private int numOfCoin;



    public Player (String nickname,DeckType assistant,TowerColor towerColor, Game gameInfo) throws FileNotFoundException{

      this.nickname = nickname;
      this.deck = new CardDeck(assistant);
      this.board = new Board(gameInfo,this,towerColor);
    }

    public String getNickname(){
        return this.nickname;
    }


    public Board getBoard(){
        return this.board;
    }


    /**Method to play a card from the player personal deck
     * @param cardToPlay card the player wants to play
     * @throws InexistentCard is the card is not present in the deck
     * @throws EndGameException if the deck is empty
     * */
    public Card playCard(Card cardToPlay) throws InexistentCard, EndGameException {
        deck.playCard(cardToPlay);
        return cardToPlay;
    }

    /**Method to modify the amount of coin of a player
     * @param quantity amount to add(if +) or subtract (if -)*/
    public void addCoin(int quantity){
        this.numOfCoin = this.numOfCoin + quantity;
    }

    public TowerColor getTowerColor() {
        return this.board.getTowerColor();
    }

    public int getNumOfCoin(){
        return numOfCoin;
    }



}
