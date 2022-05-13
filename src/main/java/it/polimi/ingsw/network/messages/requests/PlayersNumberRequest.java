package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class PlayersNumberRequest extends Message {

    public PlayersNumberRequest() {

        super(MessageType.NUMOFPLAYERS_REQUEST,"Sono Beppe Sala");

    }
}
