package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;
import org.jetbrains.annotations.TestOnly;

import java.io.FileNotFoundException;

public class Player {

    private final String nickname;
    private final Board board;
    private final CardDeck deck;
    private int numOfCoin;


    public Player (String nickname,DeckType assistant,TowerColor towerColor, Game gameInfo) {

      this.nickname = nickname;
        try {
            this.deck = new CardDeck(assistant);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.board = new Board(gameInfo,this,towerColor);
    }

    public Player () {
        this.nickname = "Default";
        this.board = null;
        this.deck = null;
    }

    public String getNickname(){
        return this.nickname;
    }


    public Board getBoard(){
        return this.board;
    }


    /**Method to play a card from the player personal deck
     * @param assistantCardToPlay card the player wants to play
     * @throws InexistentCard is the card is not present in the deck
     * @throws EndGameException if the deck is empty
     * */
    public AssistantCard playCard(AssistantCard assistantCardToPlay) throws InexistentCard, EndGameException {
        deck.playCard(assistantCardToPlay);
        return assistantCardToPlay;
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

    @TestOnly
    public DeckType getDeckType() {
        assert deck != null;
        return deck.getDeckType();
    }

    @TestOnly
    public CardDeck getDeck() {
        return deck;
    }
}
