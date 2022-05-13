package it.polimi.ingsw.network.messages.replies;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class RemainingItemReply, the controller sends this message to all the players that are still not have been created.
 *  It contains a list of the remaining items.
 * @author Miglia
 */
public class RemainingItemReply extends Message {

    UUID identifier = UUID.randomUUID();

    private final ArrayList<TowerColor> remainingTowers;
    private final ArrayList<DeckType> reamingDecks;

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
}
