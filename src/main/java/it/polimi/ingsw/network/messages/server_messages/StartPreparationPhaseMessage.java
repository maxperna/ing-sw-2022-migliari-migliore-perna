package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class StartPreparationPhaseMessage extends Message {

    public StartPreparationPhaseMessage() {

        super(MessageType.GENERIC, "Sono Beppe Sala");
    }
}

