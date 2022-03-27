package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class ThreePlayers implements StudentCreator {

    private static final int maxStudentHall = 9;
    private static final int maxTowers = 6;
    private static final int numberOfPlayers = 3;

    @Override
    public ArrayList<Player> createPlayers() {
        ArrayList<Player> playersCreated = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {

            Board board = Board.createBoard(maxStudentHall, maxTowers);
            board.setStudentsOutside(Pouch.getInstance().randomDraw(maxStudentHall));

            ArrayList<Tower> listOfTowers = new ArrayList<>();
            for (int j = 0; j < maxTowers; j++) {

                TowerColor towerColor;

                if (i == 0) {
                    towerColor = TowerColor.BLACK;
                }
                else if(i==1) {
                    towerColor = TowerColor.WHITE;
                }
                else {
                    towerColor = TowerColor.GRAY;
                }

                listOfTowers.add(new Tower(towerColor));
            }
            board.setTowers(listOfTowers);

            Player player = new Player();
            player.setBoard(board);
            playersCreated.add(player);
        }

        return playersCreated;
    }
}