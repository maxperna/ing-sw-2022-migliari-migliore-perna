package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.client_messages.AssistantCardMessage;

import java.util.ArrayList;

public class AssistantCardsMessage extends Message {
    ArrayList<AssistantCard> deck;

    public AssistantCardsMessage(ArrayList<AssistantCard> deck){
        super(MessageType.ASSISTANT_INFO,"server");
        this.deck = deck;
    }

    public ArrayList<AssistantCard> getDeck() {
        return deck;
    }
}
