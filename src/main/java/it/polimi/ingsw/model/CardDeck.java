package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CardDeck {

    private DeckType deckID;
    private ArrayList<Card> cards;
    private Card lastCardUsed;

    public ArrayList<Card> getRemainingCards(){
        return cards;
    }

    public DeckType getDeckType(){
        return this.deckID;
    }

    public Card getLastCard(){
        return lastCardUsed;
    }

    public void removeCard(){

    }

    public CardDeck(){}

}
