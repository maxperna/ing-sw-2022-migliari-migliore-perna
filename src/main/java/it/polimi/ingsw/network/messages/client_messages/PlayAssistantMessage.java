package it.polimi.ingsw.network.messages.client_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

public class PlayAssistantMessage extends Message {
    private final int playedCard;
    UUID identifier = UUID.randomUUID();

    public PlayAssistantMessage(String senderPlayer, int playedCard) {
        super(MessageType.PLAY_ASSISTANT_CARD, senderPlayer);
        this.playedCard = playedCard;
    }

    public int getPlayedCard() {
        return playedCard;
    }
}
