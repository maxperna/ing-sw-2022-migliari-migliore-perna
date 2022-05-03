package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PreparationPhaseLogic;
import it.polimi.ingsw.exceptions.CardAlreadyPlayed;
import it.polimi.ingsw.model.strategy.ThreePlayers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PreparationPhaseLogicTest {

    private static final ThreePlayers gameGenerator = new ThreePlayers();
    private static final Game gameParameter = gameGenerator.generateGame(false);



    @DisplayName("Testing round orders generators")
    @ParameterizedTest
    @MethodSource("roundOrderTestParameters")
    public void RoundOrderTest(PreparationPhaseLogic roundTest, ArrayList<Player> playersTest, ArrayList<Board> boardsTest, ArrayList<Card> cardsTest){

        int i = 0; //index variable
        //test round orders
        for(Player player: playersTest){
            try {
                roundTest.setPlayedCard(cardsTest.get(i), player);
            }
            catch(CardAlreadyPlayed e){
                e.printStackTrace();
                fail();
            }
            i++;
        }
        //Verify round orders
        Queue<Player> playerCorrectOrder = new LinkedList<>();
        playerCorrectOrder.add(playersTest.get(1));
        playerCorrectOrder.add(playersTest.get(0));
        playerCorrectOrder.add(playersTest.get(2));

        assertEquals(playerCorrectOrder,roundTest.getPlayersOrders());

    }

    @Test
    public void GeneratePlayingOrderTest(){
        PreparationPhaseLogic roundTest = new PreparationPhaseLogic(gameParameter);
        roundTest.generatePlayingOrder();
       Queue<Player> playersOrders =  roundTest.getPlayersOrders();

    }


    private static Stream<Arguments> roundOrderTestParameters() {
        //Game instance test
        //PreparationPhaseLogic test instance

        //Cards set for test
        Card card1 = new Card(4, 2, "a", "b");
        Card card2 = new Card(1, 2, "a", "b");
        Card card3 = new Card(7, 2, "a", "b");

        ArrayList<Card> cardParameters = new ArrayList<>();
        Collections.addAll(cardParameters, card1, card2, card3);

        PreparationPhaseLogic preparationPhaseLogicTest = new PreparationPhaseLogic(gameParameter);

        ArrayList<Player> playersParameters = gameParameter.getPlayersList();
        ArrayList<Board> boardsParameters = new ArrayList<>();
        //Board population
        for (Player player : playersParameters) {
            boardsParameters.add(player.getBoard());
        }

        return Stream.of(
                Arguments.of(preparationPhaseLogicTest,playersParameters,boardsParameters,cardParameters)
        );

    }

}




