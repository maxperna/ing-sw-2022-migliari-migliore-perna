package it.polimi.ingsw.network.messages;

public class GameParamMessage extends Message{

    private final int numOfPlayers;
    private final boolean expertMode;

    public GameParamMessage(String senderPlayer, int numOfPlayers, boolean expertMode) {
        super(MessageType.GAMEPARAM, senderPlayer);
        this.numOfPlayers = numOfPlayers;
        this.expertMode = expertMode;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public boolean isExpertMode() {
        return expertMode;
    }
}
