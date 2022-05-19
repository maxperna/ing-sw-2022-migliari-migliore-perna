package it.polimi.ingsw.network.messages.client_messages.ExpertMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.UUID;

/**Method 1 to play expert card, only user
 * @author Massimo */
public class PlayExpertCard1 extends Message {
    UUID identifier = UUID.randomUUID();

    public PlayExpertCard1(String senderPlayer) {

        super(MessageType.PLAY_EXPERT_CARD, senderPlayer);
    }
}
