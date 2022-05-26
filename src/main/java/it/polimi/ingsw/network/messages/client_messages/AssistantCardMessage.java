package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

public class AssistantCardMessage extends Message {
    UUID identifier = UUID.randomUUID();
    private final int playedCard;

    public AssistantCardMessage(String senderPlayer,int playedCard){
        super(MessageType.PLAY_ASSISTANT_CARD,senderPlayer);
        this.playedCard = playedCard;
    }

    public int getPlayedCard(){
        return playedCard;
    }
}
