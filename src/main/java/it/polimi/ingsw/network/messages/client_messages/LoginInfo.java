package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Reply to login request setting player nickname*/
public class LoginInfo extends Message {


    public LoginInfo(String senderPlayer){
        super(MessageType.LOGIN,senderPlayer);

    }

}
