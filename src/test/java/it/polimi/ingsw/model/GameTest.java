package it.polimi.ingsw.model;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {



    @ParameterizedTest
    @MethodSource("gameModeList")
    void startGame() {
    }

    private static List<String> gameModeList() {
        return Arrays.asList()
    }
}