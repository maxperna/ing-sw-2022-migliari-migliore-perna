package it.polimi.ingsw.model.factory;



/**
 * Class GameFactory, it allows selecting which type of logic use to create the components of the game
 * @author Miglia
 */
public class GameFactory {

    /**
     * createPlayers
     * selects the method to implement in PlayerCreator
     * @param selector used to select the set of rules to use
     * @return a factory method of the chosen type
     */
    public PlayerCreator createPlayers(String selector){
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

    /**
     * createField
     * selects the method to implement in GameFieldCreator
     * @param selector used to select the set of rules to use
     * @return a factory method of the chosen type
     */
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
