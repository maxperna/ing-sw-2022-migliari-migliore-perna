package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.UUID;

public class ExpertModeNotify extends Message {
    UUID identifier = UUID.randomUUID();
    boolean expertMode;


    public ExpertModeNotify(Boolean expertMode){
        super(MessageType.GAMEPARAM,"Server");
        this.expertMode = expertMode;
    }

    public boolean  getExpertMode() {
        return expertMode;
    }
}
