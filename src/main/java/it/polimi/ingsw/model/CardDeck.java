package it.polimi.ingsw.model;

import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;



public class CardDeck {

    private final DeckType deckCharacter;
    private ArrayList<Card> deck;
    private Card lastCardUsed;

    public CardDeck(DeckType deckCharacter) throws FileNotFoundException {
        this.deckCharacter = deckCharacter;

        //Creating a gson desarialization object
        Gson gson = new Gson();
        //JSON reader from file
        FileReader cardJSON = new FileReader("src/main/Assets/cardsJSON.json");
        //Generating a json element and relative json object
        JsonElement cardJSONFile = JsonParser.parseReader(cardJSON);
        JsonObject cardJSONObject = cardJSONFile.getAsJsonObject();
        //Getting back IMG from first field of JSON file
        String backIMGPath = cardJSONObject.get(this.deckCharacter.getCharactersName()).getAsString();
        //Getting JSON array of assistant
        JsonArray assistantsJSONArray = cardJSONObject.get("assistantProfile").getAsJsonArray();

        for(JsonElement assistantJSONElement: assistantsJSONArray){

            //Getting the fields of the assistant one by one
            JsonObject assistantJSONObject = assistantJSONElement.getAsJsonObject();
            int assistantActionNumber = assistantJSONObject.get("actionNumber").getAsInt();
            int assistantMNControl = assistantJSONObject.get("actionNumber").getAsInt();
            String assistantFrontImage= assistantJSONObject.get("frontIMG").getAsString();

            deck.add(new Card(assistantActionNumber,assistantMNControl,assistantFrontImage,backIMGPath));
        }
    }

    public ArrayList<Card> getRemainingCards(){
        return deck;
    }

    public DeckType getDeckType(){
        return this.deckCharacter;
    }

    public Card getLastCard(){
        return lastCardUsed;
    }

    public void removeCard(){

    }

}
