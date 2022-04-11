package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


/**Method to create a deck complete of 10 assistants cards and the method to use it
 * @author Massimo
 */

public class CardDeck {

    private final DeckType deckCharacter;
    private ArrayList<Card> deck;
    private Card lastCardUsed;

    /**CardDeck constructor, parse the 10 cards on a JSON file using GSON library for parse JSON file
     * @param deckCharacter character of the player deck
     */
    public CardDeck(DeckType deckCharacter) throws FileNotFoundException {
        this.deckCharacter = deckCharacter;
        this.lastCardUsed = null;

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

    /**Get the current state of the deck
     * */
    public ArrayList<Card> getRemainingCards(){
        return deck;
    }

    public DeckType getDeckType(){
        return this.deckCharacter;
    }

    /**Last card used by the player
     */
    public Card getLastCard(){
        return lastCardUsed;
    }


    /**Method to play a card from the deck
     * @param cardPlayed card choose to be played
     * @exception EndGameException signal the end of the game if the deck is empty
     * @exception InexistentCard exception thrown if played card is not present in the deck
     * */
    public void playCard(Card cardPlayed) throws InexistentCard, EndGameException {

        if(deck.size()!= 0){           //check of the emptiness of the deck
            if(deck.remove(cardPlayed)){      //check if the card is still in the deck
                lastCardUsed = cardPlayed;
            }
            else throw new InexistentCard("Card not present");
        }
        else throw new EndGameException("Run out of cards");

    }

}
