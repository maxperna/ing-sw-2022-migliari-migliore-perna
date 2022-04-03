package it.polimi.ingsw.model;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CardDeck {

    private DeckType deckID;
    private ArrayList<Card> deck;
    private Card lastCardUsed;

    Gson gson = new Gson();

    public CardDeck(DeckType deckID) throws FileNotFoundException {
        //JSON reader from file
        FileReader jsonParser = new FileReader("src/main/Assets/cardsJSON.json");
        //Generating json reading target type object
        Type assistantList = new TypeToken<Collection<Map<String,String>>>(){}.getType();
        Collection<Map<String,String>> assistantProfile = gson.fromJson(jsonParser,assistantList);  //implement json file

        //Generating deck
        for(Map<String,String> assistant:assistantProfile){
            Card newCard = new Card(assistant.get("actionNumber"),assistant.get("motherNatureControl"),assistant.get("frontIMG"),);
            deck.add(newCard);
        }
    }

    public ArrayList<Card> getRemainingCards(){
        return deck;
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
