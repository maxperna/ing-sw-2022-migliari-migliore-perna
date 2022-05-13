package it.polimi.ingsw.network.messages;

public class ConnectMessage extends Message{

    ConnectMessage(String senderPlayer) {
        super(MessageType.CONNECT, senderPlayer);
    }
}
