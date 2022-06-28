package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;


/**
 * Message used to send a board request to the server
 */
public class BoardInfoRequest extends Message {


    public BoardInfoRequest(String senderPlayer) {
        super(MessageType.SHOW_BOARD, senderPlayer);
    }
}
