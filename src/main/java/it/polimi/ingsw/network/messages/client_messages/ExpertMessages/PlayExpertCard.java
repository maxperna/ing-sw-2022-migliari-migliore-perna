package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * Abstract class extending message for get the play expert card message
 */
public abstract class PlayExpertCard extends Message {
    private final int expID;
    private final int playedCard;

    protected PlayExpertCard(int expID, int playedCard, String senderPlayer) {
        super(MessageType.PLAY_EXPERT_CARD, senderPlayer);
        this.expID = expID;
        this.playedCard = playedCard;
    }

    public int getExpID() {
        return expID;
    }

    public int getPlayedCard() {
        return playedCard;
    }
}
