package it.polimi.ingsw.model.factory;



public class GameFactory {

    public StudentCreator createPlayers(String selector){
        if (selector == null || selector.isEmpty())
            return null;
        switch (selector) {
            case "TwoPlayers":
                return new TwoPlayers();
            case "ThreePlayers":
                return new ThreePlayers();
            case "FourPlayers":
                return new FourPlayer();
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
