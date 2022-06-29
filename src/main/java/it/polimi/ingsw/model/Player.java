package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NonexistentCard;
import org.jetbrains.annotations.TestOnly;

import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Class that represents an active player, contains a reference to a board, a nickname, a cardDeck and the number of coins owned by the player
 */
public class Player implements Serializable {

    private final String nickname;
    private final Board board;
    private final CardDeck deck;
    private int numOfCoin;

    /**
     * Default constructor
     * @param nickname is the player nickname
     * @param assistant is the chosen deckType
     * @param towerColor is the chosen tower color
     * @param gameInfo is the game instance
     */
    public Player(String nickname, DeckType assistant, TowerColor towerColor, Game gameInfo) {

        this.nickname = nickname;
        try {
            this.deck = new CardDeck(assistant);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.board = new Board(gameInfo, this, towerColor);
    }

    public Player() {
        this.nickname = "Default";
        this.board = null;
        this.deck = null;
    }

    /**
     * Getter
     * @return the player's nickname
     */
    public String getNickname() {
        return this.nickname;
    }


    /**
     * Getter
     * @return the player's board
     */
    public Board getBoard() {
        return this.board;
    }


    /**
     * Method to play a card from the player personal deck
     *
     * @param assistantCardToPlay card the player wants to play
     * @throws NonexistentCard   is the card is not present in the deck
     * @throws EndGameException if the deck is empty
     */
    public AssistantCard playCard(AssistantCard assistantCardToPlay) throws NonexistentCard, EndGameException {
        deck.playCard(assistantCardToPlay);
        return assistantCardToPlay;
    }

    /**
     * Method to modify the amount of coin of a player
     *
     * @param quantity amount to add(if +) or subtract (if -)
     */
    public void addCoin(int quantity) {
        this.numOfCoin = this.numOfCoin + quantity;
    }

    /**
     * Getter
     * @return the player's towerColor
     */
    public TowerColor getTowerColor() {
        return this.board.getTowerColor();
    }

    /**
     * Getter
     * @return the number of player's coins
     */
    public int getNumOfCoin() {
        return numOfCoin;
    }

    @TestOnly
    public DeckType getDeckType() {
        assert deck != null;
        return deck.getDeckType();
    }

    @TestOnly
    public CardDeck getDeck() {
        return deck;
    }
}
