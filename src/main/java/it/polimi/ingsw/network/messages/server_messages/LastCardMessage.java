package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;

/**Message to get last assistant card played by a player*/
public class LastCardMessage extends Message {

    private final Map<String, AssistantCard> lastCardMap;
    private final DeckType type;

    public LastCardMessage(Map<String, AssistantCard> lastCardMap, DeckType type) {
        super(MessageType.LAST_ASSISTANT, "Server");
        this.lastCardMap = lastCardMap;
        this.type = type;
    }

    public Map<String, AssistantCard> getLastCardMap() {
        return lastCardMap;
    }

    public DeckType getDeckType() {
        return type;
    }
}
