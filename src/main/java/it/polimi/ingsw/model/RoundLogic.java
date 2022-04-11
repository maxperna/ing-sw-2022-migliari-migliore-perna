package it.polimi.ingsw.model;

import java.util.ArrayList;

//Class to handle the logic of the round such as orders of play and played cards during prep phase
public class RoundLogic {
    private final Game currentGame;   //game associated to round logic
    private int roundID;     //specific round roundLogic is referring to
    private ArrayList<Player> playersOrder;     //ordered list which handle the players order during the round
    private ArrayList<Card> playedCards;       //list which handle the played by the player
    private Player lastRoundFirstPlayer;       //first player to play last round, used to define the starting point of the round

    public RoundLogic(Game currentGame){
        this.currentGame = currentGame;
    }

    //Method to set the active player during the current phase of the game
    public void setActivePlayer(Player activePlayer){};

    //Method to define the order of the players during the round
    public void defineRoundOrders(){};

    public void setPlayedCard(Card playedCard){
        this.playedCards.add(playedCard);
        try{
            if(playedCards.get(playedCards.size()-2).getActionNumber()>playedCard.getActionNumber()) {
                Card smallestCard = playedCard;
            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            Card smallestCard = playedCard;
        }
    }

    //Method to generate a random player to start the first round of the game
    public void generateRandomOrder(){};
}
