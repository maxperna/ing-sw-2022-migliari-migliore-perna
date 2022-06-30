package it.polimi.ingsw.network.messages.server_messages;

import it.polimi.ingsw.network.messages.Message;

import static it.polimi.ingsw.network.messages.MessageType.AVAILABLE_ACTION;

public class AvailableActionMessage extends Message {

    private final boolean allStudentsMoved;
    private final boolean motherNatureMoved;
    private final boolean expertPlayed;

    public AvailableActionMessage(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed) {
        super(AVAILABLE_ACTION, "Server");
        this.allStudentsMoved = allStudentsMoved;
        this.motherNatureMoved = motherNatureMoved;
        this.expertPlayed = expertPlayed;
    }

    public boolean areAllStudentsMoved() {
        return allStudentsMoved;
    }

    public boolean isMotherNatureMoved() {
        return motherNatureMoved;
    }

    public boolean isExpertPlayed() {
        return expertPlayed;
    }
}
