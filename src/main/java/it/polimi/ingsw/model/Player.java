package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;

import java.io.FileNotFoundException;
import java.util.UUID;

public class Player {

    private final String nickname;
    private final Board board;
    private final CardDeck deck;
    private int numOfCoin;



    public Player (String nickname,DeckType assistant, Game gameInfo) throws FileNotFoundException {

      this.nickname = nickname;
      this.deck = new CardDeck(assistant);
      this.board = new Board(gameInfo,this);
    }

    public String getNickname(){
        return this.nickname;
    }


    public Board getBoard(){
        return this.board;
    }


    public Card playCard(Card cardToPlay) throws InexistentCard, EndGameException {
        if(deck.getRemainingCards().size()==0)
            throw new EndGameException("No more cards in the deck");
        else {
            try{
                deck.playCard(cardToPlay);
                return cardToPlay;
            }catch (InexistentCard e){
                throw new InexistentCard();
            }


        }
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
