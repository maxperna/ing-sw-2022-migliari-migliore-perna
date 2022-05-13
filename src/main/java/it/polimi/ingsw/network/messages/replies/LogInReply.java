package it.polimi.ingsw.network.messages.replies;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

public class LogInReply extends Message {

    UUID identifier = UUID.randomUUID();

    private final ArrayList<TowerColor> remainingTowers;
    private final ArrayList<DeckType> reamingDecks;

    public LogInReply( ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        super(MessageType.LOGIN_REPLY, "Sono Beppe Sala");
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
