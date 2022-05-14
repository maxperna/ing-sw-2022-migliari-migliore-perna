package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Class GameParamRequest, the controller sends this message to the first logged players.
 *  It contains the parameters to create a game.
 * @author Miglia
 */
public class GameParamRequest extends Message {

    public GameParamRequest() {

        super(MessageType.GAMEPARAM_REQUEST,"Sono Beppe Sala");

    }
}
