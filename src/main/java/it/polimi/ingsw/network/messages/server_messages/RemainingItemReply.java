package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class RemainingItemReply, the controller sends this message to all the players that are still not have been created.
 * It contains a list of the remaining items.
 *
 * @author Miglia
 */
public class RemainingItemReply extends Message {

    private final ArrayList<TowerColor> remainingTowers;
    private final ArrayList<DeckType> reamingDecks;
    UUID identifier = UUID.randomUUID();

    public RemainingItemReply(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        super(MessageType.REMAINING_ITEM, "Sono Beppe Sala");
        this.remainingTowers = remainingTowers;
        this.reamingDecks = remainingDecks;
    }

    public ArrayList<TowerColor> getRemainingTowers() {
        return remainingTowers;
    }

    public ArrayList<DeckType> getReamingDecks() {
        return reamingDecks;
    }

    public static class ExpertCardPlay extends Message {
        private final int expertToPlay;
        UUID identifier = UUID.randomUUID();

        public ExpertCardPlay(String user, int expertToPlay) {
            super(MessageType.PLAY_EXPERT_CARD, user);
            this.expertToPlay = expertToPlay;
        }

        public int getExpertToPlay() {
            return expertToPlay;
        }
    }
}
