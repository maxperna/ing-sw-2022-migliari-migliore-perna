package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.Map;

/**Message to get last assistant card played by a player*/
public class LastCardMessage extends Message {

    private final Map<String, AssistantCard> lastCardMap;

    public LastCardMessage(Map<String, AssistantCard> lastCardMap) {
        super(MessageType.LAST_ASSISTANT, "Server");
        this.lastCardMap = lastCardMap;
    }

    public Map<String, AssistantCard> getLastCardMap() {
        return lastCardMap;
    }

}
