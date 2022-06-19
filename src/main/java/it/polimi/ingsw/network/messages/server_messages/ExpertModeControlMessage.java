package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class ExpertModeControlMessage extends Message {
    private final boolean setExpertMode;

    public ExpertModeControlMessage(boolean setExpertMode) {
        super(MessageType.EXPERT_MODE_CONTROL, "Server");
        this.setExpertMode = setExpertMode;
    }

    public boolean isSetExpertMode() {
        return setExpertMode;
    }
}
