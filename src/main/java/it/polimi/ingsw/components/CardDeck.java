package it.polimi.ingsw.components;

import it.polimi.ingsw.Assistants;
import it.polimi.ingsw.DeckType;

import java.util.ArrayList;

public class CardDeck {

    private DeckType deckID;
    private ArrayList<Assistants> cards;
    private Card lastCardUsed;

    public ArrayList<Assistants> getRemainingCards(){
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
