package it.polimi.ingsw.assistants;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertsFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

public class AssistantFactoryTest {

    Game test = GameManager.getInstance().startGame("TwoPlayers",false);
    ExpertsFactory factoryTest = ExpertsFactory.createFactory(test);
        @Test
        public void drawAssistantTest(){
        //check for five times if duplicates happen and if ExpertsFactory is actually a singleton
        for(int i=0;i<5;i++){
            ArrayList<ExpertCard> testCards = factoryTest.drawAssistant();
            ArrayList<ExpertCard> oldTestCards = new ArrayList<>();    //old iteration card list

            //Getting the classes of the generated cards in order to check possible duplicates
            ArrayList<Object> testCardsClass = new ArrayList<>();
            for(ExpertCard card: testCards){
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
