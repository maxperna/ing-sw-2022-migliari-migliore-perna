package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Class EndLogInMessage, the controller sends this message to all the players.
 *  Notifies that the LoginPhase is completed.
 * @author Miglia
 */
public class EndLogInMessage extends Message {

    public EndLogInMessage() {
        super(MessageType.ENDLOGIN, "Sono Beppe Sala");
    }
}
