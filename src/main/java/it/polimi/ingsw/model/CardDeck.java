package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NonexistentCard;
import org.jetbrains.annotations.TestOnly;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class that contains initially 10 assistant cards
 *
 * @author Massimo
 */

public class CardDeck implements Serializable {

    private final DeckType deckCharacter;
    private final ArrayList<AssistantCard> deck;
    private AssistantCard lastAssistantCardUsed;

    /**
     * CardDeck constructor, parse the 10 cards on a JSON file using GSON library
     *
     * @param deckCharacter character of the player deck
     */
    public CardDeck(DeckType deckCharacter) throws FileNotFoundException {
        this.deckCharacter = deckCharacter;
        this.deck = new ArrayList<>();
        //Creating a gson deserialization object
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

        for (JsonElement assistantJSONElement : assistantsJSONArray) {

            //Getting the fields of the assistant one by one
            JsonObject assistantJSONObject = assistantJSONElement.getAsJsonObject();
            int assistantActionNumber = assistantJSONObject.get("actionNumber").getAsInt();
            int assistantMNControl = assistantJSONObject.get("motherNatureControl").getAsInt();
            String assistantFrontImage = assistantJSONObject.get("frontIMG").getAsString();

            this.deck.add(new AssistantCard(assistantActionNumber, assistantMNControl, assistantFrontImage, backIMGPath, deckCharacter));
        }
    }

    /**
     * Method used to play an assistantCard
     * @param assistantCardPlayed is the card played
     * @throws NonexistentCard when the card is not available
     * @throws EndGameException when there are no assistantCards left
     */
    public void playCard(AssistantCard assistantCardPlayed) throws NonexistentCard, EndGameException {
        if (!deck.remove(assistantCardPlayed)) {
            if (deck.size() == 0)
                throw new EndGameException();
            throw new NonexistentCard();
        }

        lastAssistantCardUsed = assistantCardPlayed;
    }

    public ArrayList<AssistantCard> getRemainingCards() {
        return deck;
    }

    public DeckType getDeckType() {
        return this.deckCharacter;
    }

    public AssistantCard getLastCard() {
        return lastAssistantCardUsed;
    }

    @TestOnly
    public ArrayList<AssistantCard> getDeck() {
        return deck;
    }
}
