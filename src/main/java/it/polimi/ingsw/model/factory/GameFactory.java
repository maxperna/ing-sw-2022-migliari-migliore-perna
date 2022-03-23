package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.factory.BoardCreator;
import it.polimi.ingsw.model.factory.FourPlayerBoard;
import it.polimi.ingsw.model.factory.ThreePlayersBoard;
import it.polimi.ingsw.model.factory.TwoPlayersBoard;

public class GameFactory {

    public BoardCreator createBoard(String selector){
        if (selector == null || selector.isEmpty())
            return null;
        switch (selector) {
            case "TwoPlayers":
                return new TwoPlayersBoard();
            case "ThreePlayers":
                return new ThreePlayersBoard();
            case "FourPlayers":
                return new FourPlayerBoard();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }
}
