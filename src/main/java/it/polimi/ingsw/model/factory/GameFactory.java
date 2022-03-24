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
                return new FourPlayerBoard();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }

    public GameFieldCreator createField(String selector){
        if (selector == null || selector.isEmpty())
            return null;
        switch (selector) {
            case "TwoPlayers":
                return new TwoPlayersField();
            case "ThreePlayers":
                return new ThreePlayersField();
            case "FourPlayers":
                return new FourPlayersFIeld();
            default:
                throw new IllegalArgumentException("Unknown selector "+selector);
        }
    }
}
