package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class FourPlayer implements StudentCreator {

    private static final int maxStudentHall = 7;
    private static final int maxTowers = 8;
    private static final int numberOfPlayers = 4;

    @Override
    public ArrayList<Player> createPlayers() {
        ArrayList<Player> playersCreated = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers/2; i++) {

            Board board = Board.createBoard(maxStudentHall,maxTowers);
            board.setStudentsOutside(Pouch.getInstance().randomDraw(maxStudentHall));

            ArrayList<Tower> listOfTowers = new ArrayList<>();
            for (int j = 0; j < maxTowers; j++) {

                TowerColor towerColor;

                if(i == 0) {
                    towerColor = TowerColor.BLACK;
                }
                else {
                    towerColor = TowerColor.WHITE;
                }

                listOfTowers.add(new Tower(towerColor));
            }
            board.setTowers(listOfTowers);

            Player player = new Player();
            player.setBoard(board);
            playersCreated.add(player);
        }

        for (int k = 0; k < numberOfPlayers/2; k++) {

            Board board = Board.createBoard(maxStudentHall,maxTowers);
            board.setStudentsOutside(Pouch.getInstance().randomDraw(maxStudentHall));

            board.setTowers(playersCreated.get(k).getBoard().getTowers());

            Player player = new Player();
            player.setBoard(board);
            playersCreated.add(player);
        }

        return playersCreated;
    }
}