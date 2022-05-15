package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.security.InvalidParameterException;
import java.util.*;

/**Class to handle all the logic inside a round such as the playing orders and to set the current active player
 * @author Massimo
 * */
public class TurnLogic {
    private final Game currentGame;   //game associated to round logic
    private final Map<Integer, Player> cardsPlayed;
    private final Queue<Player> playersOrders;  //Players order is a FIFO structure(both for playing orders and action phase)
    private Player lastRoundFirstPlayer;       //first player to play last round, used to define the starting point of the round
    private Player activePlayer;     //current active player
    private String currentPhase;

    //Default constructor
    public TurnLogic(Game currentGame){
        this.currentGame = currentGame;
        this.cardsPlayed = new HashMap<>();
        this.playersOrders = new LinkedList<>();
        this.currentPhase = "PREPARATION";       //Always start in preparation phase
    }

    /**Method to set the current active player after the preparation phase or the action phase
     */
    public Player currentActivePlayer(){
        if(playersOrders.size() == currentGame.NUM_OF_PLAYERS){
            this.lastRoundFirstPlayer = playersOrders.peek();
        }
        this.activePlayer = playersOrders.remove();
        return activePlayer;
    }


    /**Method to define round order in the action phase comparing played cards action number
     * */
    private void defineActionPhaseOrders(){
        switchPhase();
        playersOrders.clear();
        //sorting the list from the highest action number to the lowest for a matter comfort
        List<Integer> roundCards = new ArrayList<>(cardsPlayed.keySet());
        Collections.sort(roundCards);

        for(Integer card:roundCards){
            this.playersOrders.add(cardsPlayed.get(card));
        }

    }

    /**Method to keep track of the played card during a round preparation phase, inserting by their action number
     * @param playedAssistantCard card used by a certain player
     * @param player player who used the card
     * @exception CardAlreadyPlayed if a player try to use a card already used by another player within the same round
     */
    public void setPlayedCard(AssistantCard playedAssistantCard, Player player) throws CardAlreadyPlayed, InexistentCard, EndGameException {
        if(!this.cardsPlayed.containsKey(playedAssistantCard.getActionNumber())) {
            this.cardsPlayed.put(playedAssistantCard.getActionNumber(), player);
            player.playCard(playedAssistantCard);
        }
        else throw new CardAlreadyPlayed("Another player already used this card");

        //All players have played their cards
        if(cardsPlayed.size()==currentGame.NUM_OF_PLAYERS){
            defineActionPhaseOrders();
        }
    }

    /**Method to set a random players order during the first round of the game or an order based on the last first player
     * on the previous round( always in anticlockwise sense)
     * */
    public void generatePreparationPhaseOrder(){
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
        //branch to generate playing order during game based on last round first player
        else{
            int index = currentGame.getPlayersList().indexOf(lastRoundFirstPlayer);
            while(this.playersOrders.size()!=currentGame.NUM_OF_PLAYERS){
                this.playersOrders.add(currentGame.getPlayersList().get(index));
                index = (index+1)%currentGame.NUM_OF_PLAYERS;
            }
        }

    }

    /**Method to switch the current phase (from preparation to action or vice-versa)*/
    private void switchPhase(){
        if(currentPhase.equals("PREPARATION"))
            currentPhase = "ACTION";
        else
            currentPhase = "PREPARATION";
    }

    /**Method to end current turn and start a new one*/
    public void endTurn(){
        playersOrders.clear();
        cardsPlayed.clear();
    }

    public String getCurrentPhase(){
        return currentPhase;
    }

    public Player getActivePlayer(){
        return this.activePlayer;
    }

    public Queue<Player> getPlayersOrders() {
        return playersOrders;
    }

    public Map<Integer, Player> getCardsPlayed() {
        return cardsPlayed;
    }

    //ACTION PHASE PART

    public void moveStudentOnBoard(Player player, Color color){
        try {
            player.getBoard().moveEntryToDiningRoom(color);
        } catch (NotOnBoardException | NotEnoughSpace e) {
            throw new RuntimeException(e);
        }
    }

    public void moveStudentToIsland(Player player, Color color, int nodeID){
        try {
            player.getBoard().moveToIsland(color,nodeID);
        } catch (NotOnBoardException e) {
            throw new RuntimeException(e);
        }
    }

    /**Method to move mother nature over the island list
     * */
    public void moveMotherNature(Player player,int islandID){
        try {
            currentGame.getGameField().moveMotherNatureToNodeID(islandID);
        }
        catch(InvalidParameterException | EndGameException e){
            e.printStackTrace();
        }
    }

    public void chooseCloudTile(Player player,int cloudID){
        try {
            ArrayList<Color> pickedStudents = new ArrayList<>(currentGame.getCloudTiles().get(cloudID).getStudents());
            player.getBoard().addStudentsEntryRoom(pickedStudents);
        }
        catch (EmptyCloudException | NotEnoughSpace e){
            e.printStackTrace();
        }

    }

    public void playExpertCard(Player player){

    }

    public void playExpertCard(Player player,int nodeId){

    }
}










