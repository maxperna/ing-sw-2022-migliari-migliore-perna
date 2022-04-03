package it.polimi.ingsw.model;

/**
 * Enum for the 4 kind of deck characters
 * @author Massimo
 */
public enum DeckType {
    DRUID("druid"),
    KING("king"),
    WITCH("witch"),
    SAGE("sage");

    private final String charactersName;

    private DeckType(String charactersName){
        this.charactersName = charactersName;
    }

    public String getCharactersName() {
        return charactersName;
    }
}
