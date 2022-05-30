package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class CliTest {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String CLEAR = "\033[H\033[2J";

    @Test
    void checkBoardCreation() {
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("TwoPlayers", false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            if (game.NUM_OF_PLAYERS == 3)
                game.addPlayer("Pippo", DeckType.KING, TowerColor.GRAY);

            game.getPlayerByNickName("Piero").getBoard().addStudentsDiningRoom(game.getPlayerByNickName("Piero").getBoard().getEntryRoom());
            int upperLimit;

            if (game.getPlayersList().size() == 2)
                upperLimit = 6;
            else
                upperLimit = 8;

            int k = 0;
            Color[] color = Color.values();
            int color_index = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 108; j++) {
                    if (i == 0 || i == 5)
                        System.out.print("_");
                    else if (i == 1 && j == 0)
                        System.out.println();
                    else if (j == 0 || j == 6) {
                        if (k < upperLimit) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + toColor(game.getPlayerByNickName("Piero").getBoard().getEntryRoom().get(k), "██ ") + "     " + toColor(game.getPlayerByNickName("Piero").getBoard().getEntryRoom().get(++k), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == upperLimit) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + toColor(game.getPlayerByNickName("Piero").getBoard().getEntryRoom().get(k), "██ ") + "              |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == 7) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|                       |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            i++;
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NotEnoughSpace e) {
            e.printStackTrace();
        }
    }

    public String toColor(Color color, String string) {
        switch (color) {
            case RED:
                return ANSI_RED + string + ANSI_RESET;
            case BLUE:
                return ANSI_BLUE + string + ANSI_RESET;
            case GREEN:
                return ANSI_GREEN + string + ANSI_RESET;
            case YELLOW:
                return ANSI_YELLOW + string + ANSI_RESET;
            case PINK:
                return ANSI_PINK + string + ANSI_RESET;
        }
        return "";
    }


    public void printStudentInsideDiningRoom(Color color, int numOfColor, Boolean teacher) {

        for (int i = 1; i < 11; i++) {
            if (i < numOfColor + 1) {
                System.out.print("   " + toColor(color, "●") + "   ");
            } else if (i % 3 == 0 && i>numOfColor +1)
                System.out.print("   ?   ");
            else if (i % 3 == 0 && i < numOfColor +1)
                System.out.print("   "+toColor(color,"⦾" + ANSI_RESET + "   |"));
            else if (i != 10)
                System.out.print("   -   ");
            else if (teacher)
                System.out.print("   -       |   " + toColor(color, "●" + ANSI_RESET + "   |"));
            else
                System.out.print("   -       |   -   |");
        }

    }
}
