package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


/**Method to create a deck complete of 10 assistants cards
 * @author Massimo
 */

public class CardDeck {

    private final DeckType deckCharacter;
    private final ArrayList<Card> deck = new ArrayList<>();
    private Card lastCardUsed;

    /**CardDeck constructor, parse the 10 cards on a JSON file using GSON library for parse JSON file
     * @param deckCharacter character of the player deck
     */
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
        JsonObject backIMG = cardJSONObject.get("deckType").getAsJsonObject();
        String backIMGPath = backIMG.get(this.deckCharacter.getCharactersName()).getAsString();
        //Getting JSON array of assistant
        JsonArray assistantsJSONArray = cardJSONObject.get("assistantProfile").getAsJsonArray();

        for(JsonElement assistantJSONElement: assistantsJSONArray){

            //Getting the fields of the assistant one by one
            JsonObject assistantJSONObject = assistantJSONElement.getAsJsonObject();
            int assistantActionNumber = assistantJSONObject.get("actionNumber").getAsInt();
            int assistantMNControl = assistantJSONObject.get("actionNumber").getAsInt();
            String assistantFrontImage= assistantJSONObject.get("frontIMG").getAsString();

            this.deck.add(new Card(assistantActionNumber,assistantMNControl,assistantFrontImage,backIMGPath));
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

    public Card playCard(Card cardToPlay) throws InexistentCard, EndGameException {
        if(deck.size()==0)
            throw new EndGameException("No more cards in the deck");
        if(deck.remove(cardToPlay)){
            lastCardUsed = cardToPlay;
            return cardToPlay;
        }
        else throw new InexistentCard("Card is not in the deck");
    }
}
