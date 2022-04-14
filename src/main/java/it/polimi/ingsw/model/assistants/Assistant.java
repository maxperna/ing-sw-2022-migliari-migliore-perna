package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.model.Player;

public abstract class Assistant {
    private boolean isPlayed;       //flag is the card has been already played
    private int cost;      //cost of play the assistant

    public void playCard(Player activePlayer, int roundID){          //effect must be attached to a player in a certain round
        this.cost++;           //add one to the cost once the card is played
    };

    public int getCost(){
        return this.cost;
    }
}
