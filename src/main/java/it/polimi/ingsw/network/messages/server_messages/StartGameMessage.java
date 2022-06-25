package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class StartGameMessage extends Message {
    public StartGameMessage(){
        super(MessageType.START_GAME,"Server");
    }
}