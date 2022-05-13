package it.polimi.ingsw.network.messages;

/**Enum to define message type
 * @author Massimo*/
public enum MessageType {

    LOGIN, //Creates VirtualView
    ENDLOGIN, //Notify the end of loginPhase
    GAMEPARAM, //Param to create game (only first LogIn)
    GAMEPARAM_REQUEST,
    PLAYER_CREATION, //Param to create Players
    REMAINING_ITEM, //Sends to other players the remaining items (Deck and Tower)

}
