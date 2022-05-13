package it.polimi.ingsw.network.messages;

public class PlayersNumberMessage extends Message{

    private final int numOfPlayers;

    public PlayersNumberMessage(String senderPlayer, int numOfPlayers) {
        super(MessageType.NUMOFPLAYERS, senderPlayer);
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
