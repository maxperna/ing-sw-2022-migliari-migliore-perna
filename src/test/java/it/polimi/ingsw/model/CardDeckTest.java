package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.InexistentCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CardDeckTest {

    @DisplayName("Test deck consistency creation")
    @Test
    void testDeckCreation() throws EndGameException {
        DeckType parameter = DeckType.DRUID;

        try{
            CardDeck deck = new CardDeck(parameter);
            assertEquals(deck.getRemainingCards().size(),10);
            assertEquals(deck.getRemainingCards().get(4).getActionNumber(),5);
            assertEquals(deck.getDeckType(),parameter);
            assertEquals(deck.getRemainingCards().get(3).getBackImage(),"1");   //1 stand for druid
            try{
                deck.playCard(deck.getRemainingCards().get(6)); //play a card and remove it
                assertEquals(deck.getLastCard().getActionNumber(),7);            //check if the removed card is correct
                assertEquals(deck.getLastCard(),deck.getLastCard());            //check if the last card is counted
                assertEquals(deck.getRemainingCards().size(),9);     //check on the removal

            }
            catch (InexistentCard e){
                e.printStackTrace();
                fail();
            }

        }
        catch (FileNotFoundException e){
            fail();
        }
    }

    @DisplayName("Test deck emptying")
    @Test
    void DeckEmptying() throws FileNotFoundException {
        DeckType parameter1 = DeckType.SAGE;
        CardDeck deck = new CardDeck(parameter1);

        //Emptying test one card by one
        try{
            //check card one by one
            for(int i=0;i<=deck.getRemainingCards().size();i++){
                deck.playCard(deck.getRemainingCards().get(0));
                assertEquals(deck.getLastCard().getActionNumber(),i+1);

            }
        }
        catch(EndGameException e){
            e.printStackTrace();
            assertEquals(deck.getRemainingCards().size(),0);
        }
        catch (InexistentCard e){
            e.printStackTrace();
            fail();
        }
    }
}
