package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class UpdateNodeMessage extends Message {

    private final Node UpdatedNode;

    public UpdateNodeMessage(Node updatedNode) {
        super(MessageType.UPDATE_NODE, "Server");
        this.UpdatedNode = updatedNode;
    }

    public Node getUpdatedNode() {
        return UpdatedNode;
    }
}
