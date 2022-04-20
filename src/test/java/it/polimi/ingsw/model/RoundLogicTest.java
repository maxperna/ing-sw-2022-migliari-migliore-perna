package it.polimi.ingsw.model;

import it.polimi.ingsw.model.strategy.ThreePlayers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import it.polimi.ingsw.model.strategy.ThreePlayers.*;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class RoundLogicTest{

    @Test
    @DisplayName("Testing round orders generators")
    @ParameterizedTest
    @MethodSource("testParameters")
    public void RoundOrderTest(){


    }

    private static Stream<Arguments> testParameters() {
        //Game instance test
        //RoundLogic test instance

        //Cards set for test
        Card card1 = new Card(1, 2, "a", "b");
        Card card2 = new Card(4, 2, "a", "b");
        Card card3 = new Card(7, 2, "a", "b");

        ArrayList<Card> cardParameters = new ArrayList<>();
        Collections.addAll(cardParameters, card1, card2, card3);

        ThreePlayers gameGenerator = new ThreePlayers();
        Game gameParameter = gameGenerator.generateGame();
        RoundLogic roundLogicTest = new RoundLogic(gameParameter);

        ArrayList<Player> playersParameters = gameParameter.getPlayersList();
        ArrayList<Board> boardsParameters = new ArrayList<>();
        //Board population
        for (Player player : playersParameters) {
            boardsParameters.add(player.getBoard());
        }

        return Stream.of(
                Arguments.of(gameParameter),
                Arguments.of(roundLogicTest),
                Arguments.of(playersParameters),
                Arguments.of(boardsParameters),
                Arguments.of(cardParameters)
        );

    }
}




