package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.gameField.IsladNode;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class UpdateNodeMessage extends Message {

    private final IsladNode UpdatedNode;

    public UpdateNodeMessage(IsladNode updatedNode) {
        super(MessageType.UPDATE_NODE, "Server");
        this.UpdatedNode = updatedNode;
    }

    public IsladNode getUpdatedNode() {
        return UpdatedNode;
    }
}
