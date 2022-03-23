package it.polimi.ingsw.model.factory;

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
                return new FoursPlayerBoard();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }
}
