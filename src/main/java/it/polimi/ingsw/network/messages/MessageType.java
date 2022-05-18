package it.polimi.ingsw.network.messages;

/**Enum to define message type
 * @author Massimo*/
public enum MessageType {
    /**Creates VirtualView*/
    LOGIN,
    /**Notify the end of loginPhase*/
    ENDLOGIN,
    /**Param to create game (only first LogIn)*/
    GAMEPARAM,
    /**Request for game parameters*/
    GAMEPARAM_REQUEST,
    /**Request for player creation parameters*/
    PLAYER_CREATION,
    /**Sends to other players the remaining items (Deck and Tower)*/
    PLAYER_INIT,
    /**Contains the info of the player at the beginning of the game*/
    CURRENT_PLAYER,
    /**Contains the info of the next player that will play*/
    CHARGECLOUD,
    /**Contains the info of the clouds at the beginning of preparationPhase*/
    REMAINING_ITEM,
    /**Move a student from board to island*/
    MOVE_TO_ISLAND,
    /**Move a student from entrance to dining room*/
    MOVE_TO_DINING,
    /**Play assistant card*/
    PLAY_ASSISTANT_CARD,
    /**Play expert card*/
    PLAY_EXPERT_CARD,
    /**Send an object recognized by ID*/
    UPDATE_TEACHERS,
    /**Send a lis of a player Teachers*/
    GAME_FIELD,

    UPDATE_NODE,
    ID_SELECTION,
    /**Move mother nature on a selected island*/
    MOVE_MOTHER_NATURE,
    /**Require expert card necessary parameters*/
    GET_EXPERT_PARAM,
    /**Notify the merging of two island*/
    NOTIFY_MERGE,
    /**Notify the victory of a player*/
    NOTIFY_VICTORY,
    /**Generic error message */
    ERROR,
    /**Generic message*/
    GENERIC

}
