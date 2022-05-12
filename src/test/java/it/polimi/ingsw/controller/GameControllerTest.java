package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.FirstLoginMessage;
import it.polimi.ingsw.network.messages.LogInMessage;
import it.polimi.ingsw.network.messages.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private static Stream<String> gameModeList() {

        return Stream.of("TwoPlayers", "ThreePlayers", "FourPlayers");
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWorkExpert(String gameMode) {

        boolean expertMode = true;
        GameController gameController = new GameController(gameMode, expertMode);

        gameController.onMessageReceived(new FirstLoginMessage("Gianni", gameMode, expertMode, TowerColor.WHITE, DeckType.DRUID ));
        gameController.onMessageReceived(new LogInMessage("Beppe", TowerColor.BLACK, DeckType.SAGE));
    }

    @ParameterizedTest
    @MethodSource("gameModeList")
    public void controllerShouldWork(String gameMode) {

        GameController gameController = new GameController(gameMode, false);
    }

}