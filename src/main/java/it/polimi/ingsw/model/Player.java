package it.polimi.ingsw.model;

//Class which represents the player
public class Player {
    private String nickname;             //player nickname
    private Board playerBoard;            //board assigned to the player
    private CardDeck playerDeck;         //card deck assigned to the player

    //method to set player nickname if not already done
    public void setNickname(String nickname) {
        if(nickname== null) {
            this.nickname = nickname;
        }
    }

    //method to set player deck if not already done
    public void setPlayerDeck(CardDeck deck){
        if(playerDeck == null){
            this.playerDeck = deck;
        }
    }

    //method to get playerBoard
    public Board getPlayerBoard(){
        return playerBoard;
    }
}
