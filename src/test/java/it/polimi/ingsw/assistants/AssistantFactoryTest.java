package it.polimi.ingsw.assistants;

import it.polimi.ingsw.model.assistants.AssistantCard;
import it.polimi.ingsw.model.assistants.AssistantFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

public class AssistantFactoryTest {
    AssistantFactory factoryTest = AssistantFactory.createFactory();

    @Test
    public void drawAssistantTest(){
        //check for five times if duplicates happen and if AssistantFactory is actually a singleton
        for(int i=0;i<5;i++){
            ArrayList<AssistantCard> testCards = factoryTest.drawAssistant();
            ArrayList<AssistantCard> oldTestCards = new ArrayList<>();    //old iteration card list

            //Getting the classes of the generated cards in order to check possible duplicates
            ArrayList<Object> testCardsClass = new ArrayList<>();
            for(AssistantCard card: testCards){
                testCardsClass.add(card.getClass());
            }
            //generate a set to delete duplicates in testCards (set only contains unique elements)
            Set<Object> uniqueCards = new HashSet<>(testCardsClass);

            //compare set and list lengths, if not equal set has less element of a list(list contains duplicates)
            if(testCards.size()>uniqueCards.size())
                fail("Duplicates in list");
            //compare the previous call with the new one
            if(i!=0 && testCards.equals(oldTestCards))
                fail("Cards list is not a singleton");

            oldTestCards.addAll(testCards);
        }

    }

}
