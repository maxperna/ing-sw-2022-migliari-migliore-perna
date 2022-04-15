package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardAlreadyPlayed;

import java.util.*;

/**Class to handle all the logic inside a round such as the playing orders and to set the current active player
 * @author Massimo
 * */
public class RoundLogic {
    private final Game currentGame;   //game associated to round logic
    private int roundID;     //specific round roundLogic is referring to
    private Map<Card,Player> cardsPlayed = new HashMap<>();
    private Queue<Player> playersOrders = new LinkedList<>();  //Players order is a FIFO structure
    private Player lastRoundFirstPlayer;       //first player to play last round, used to define the starting point of the round
    private Player activePlayer;     //current active player

    //Default constructor
    public RoundLogic(Game currentGame){
        this.currentGame = currentGame;
    }

    /**Method to set the current active player after the preparation phase or the action phase
     */
    public void changeActivePlayer(){
        if(playersOrders.size() == currentGame.NUM_OF_PLAYERS){
            this.lastRoundFirstPlayer = playersOrders.peek();
        }
        this.activePlayer = playersOrders.remove();
    }


    /**Method to define round order after the preparation phase comparing played cards action number
     * */
    public void defineRoundOrders(){

        //sorting the list from the highest action number to the lowest for a matter comfort
        List<Card> roundCards = new ArrayList<>(cardsPlayed.keySet());
        roundCards.sort((a1, a2) -> {
            if (a1.getActionNumber()>a2.getActionNumber()) return 1;
            else return 0;
        });

        for(Card card:roundCards){
            this.playersOrders.add(cardsPlayed.get(card));
        }

    }

    /**Method to keep track of the played card during a round preparation phase
     * @param playedCard card used by a certain player
     * @param player player who used the card
     * @exception CardAlreadyPlayed if a player try to use a card already used by another player within the same round
     */
    public void setPlayedCard(Card playedCard,Player player) throws CardAlreadyPlayed {
        if(!this.cardsPlayed.containsKey(playedCard))
            this.cardsPlayed.put(playedCard,player);
        else throw new CardAlreadyPlayed("Another player already used this card");
    }

    /**Method to set a random players order during the first round of the game or an order based on the last first player
     * on the previous round( always in anticlockwise sense)
     * */
    public void generatePlayingOrder(){
        //branch to generate a casual order, if lastRoundFirstPlayer is null no round has been already played
        if(lastRoundFirstPlayer == null){
            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(currentGame.NUM_OF_PLAYERS);
            //Loading players orders in anticlockwise sense starting by a random number
            while(this.playersOrders.size()!=currentGame.NUM_OF_PLAYERS){
                this.playersOrders.add(currentGame.getPlayersList().get(randomNumber));
                randomNumber = (randomNumber+1)%currentGame.NUM_OF_PLAYERS;
            }
        }
        //branch to generate playing order during game base on last round first player
        else{
            int index = currentGame.getPlayersList().indexOf(lastRoundFirstPlayer);
            while(this.playersOrders.size()!=currentGame.NUM_OF_PLAYERS){
                this.playersOrders.add(currentGame.getPlayersList().get(index));
                index = (index+1)%currentGame.NUM_OF_PLAYERS;
            }
        }

    }
    public void endRound(){
        this.cardsPlayed.clear();
        this.roundID++;
    }

    public void setRoundID(int roundID){
        this.roundID = roundID;
    }
    public Player getActivePlayer(){
        return this.activePlayer;
    }
}
