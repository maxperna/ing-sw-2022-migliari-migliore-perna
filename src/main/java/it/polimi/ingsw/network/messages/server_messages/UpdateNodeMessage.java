package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class UpdateNodeMessage extends Message {

    private final IslandNode UpdatedNode;

    public UpdateNodeMessage(IslandNode updatedNode) {
        super(MessageType.UPDATE_NODE, "Server");
        this.UpdatedNode = updatedNode;
    }

    public IslandNode getUpdatedNode() {
        return UpdatedNode;
    }
}
