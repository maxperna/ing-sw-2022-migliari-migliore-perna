package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class GameParamRequest extends Message {

    public GameParamRequest() {

        super(MessageType.GAMEPARAM_REQUEST,"Sono Beppe Sala");

    }
}
