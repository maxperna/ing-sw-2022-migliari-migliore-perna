package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import java.util.UUID;

/**
 * Method 1 to play expert card, only user
 *
 * @author Massimo
 */
public class PlayExpertCard1 extends PlayExpertCard {
    UUID identifier = UUID.randomUUID();

    public PlayExpertCard1(String senderPlayer, int playedCard) {

        super(1, playedCard, senderPlayer);
    }

}
