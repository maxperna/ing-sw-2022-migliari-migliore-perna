package it.polimi.ingsw.network.messages;

public class EndLogInMessage extends Message{

    public EndLogInMessage() {
        super(MessageType.ENDLOGIN, "Sono Beppe Sala");
    }
}
