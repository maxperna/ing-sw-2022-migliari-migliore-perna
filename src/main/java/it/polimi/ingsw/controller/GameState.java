package it.polimi.ingsw.controller;

/**
 * Enum containing all the possible states for the finished state machine managing the game phase
 */
public enum GameState {
    LOGIN, //creates the game
    CREATE_PLAYERS, //creates players
    PREPARATION_PHASE,
    ACTION_PHASE,
}
