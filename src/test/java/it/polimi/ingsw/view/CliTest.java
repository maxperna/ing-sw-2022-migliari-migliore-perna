package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CliTest {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String CLEAR = "\033[H\033[2J";

    @Test
    void checkBoardCreationTwoPlayers7Students() {
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("TwoPlayers", false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            if (game.NUM_OF_PLAYERS == 3)
                game.addPlayer("Pippo", DeckType.KING, TowerColor.GRAY);


            int hallDimension;
            if (game.getPlayersList().size() == 2) {
                hallDimension = 7;
            } else {
                hallDimension = 9;
            }
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
                        if (k < hallDimension-1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1 && hallDimension == 7) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|                       |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            i++;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkBoardCreationTwoPlayers8Students() {
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("TwoPlayers", false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            if (game.NUM_OF_PLAYERS == 3)
                game.addPlayer("Pippo", DeckType.KING, TowerColor.GRAY);

            game.getPlayerByNickName("Piero").getBoard().addSingleStudent(Color.RED);
            int hallDimension;
            if (game.getPlayersList().size() == 2) {
                hallDimension = 7;
            } else {
                hallDimension = 9;
            }
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
                        if (k < hallDimension-1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1 && hallDimension == 7) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|                       |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            i++;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkBoardCreationThreePlayers9Students() {
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("ThreePlayers", false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            if (game.NUM_OF_PLAYERS == 3)
                game.addPlayer("Pippo", DeckType.KING, TowerColor.GRAY);


            int hallDimension;
            if (game.getPlayersList().size() == 2) {
                hallDimension = 7;
            } else {
                hallDimension = 9;
            }
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
                        if (k < hallDimension-1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1 && hallDimension == 7) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|                       |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            k++;
                        } else if (k == hallDimension - 1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            i++;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkBoardCreationThreePlayers10Students() {
        try {
            GameManager.getInstance().setNull();
            Game game = GameManager.getInstance().initGame("ThreePlayers", false);

            game.addPlayer("Piero", DeckType.DRUID, TowerColor.WHITE);
            game.addPlayer("Gianna", DeckType.SAGE, TowerColor.BLACK);
            if (game.NUM_OF_PLAYERS == 3)
                game.addPlayer("Pippo", DeckType.KING, TowerColor.GRAY);

            game.getPlayerByNickName("Piero").getBoard().addSingleStudent(Color.BLUE);
            System.out.println("Size: " + game.getPlayerByNickName("Piero").getBoard().getEntryRoom().size());

            int upperLimit;

            if (game.getPlayersList().size() == 2)
                upperLimit = 6;
            else
                upperLimit = 8;
            System.out.println("Upper limit: " + upperLimit);

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
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == upperLimit && game.getPlayerByNickName("Piero").getBoard().getEntryRoom().size() == upperLimit+1) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "              |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        }
                        else if (k == upperLimit && game.getPlayerByNickName("Piero").getBoard().getEntryRoom().size() == upperLimit+2) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            color_index++;
                            System.out.println();
                            k++;
                        } else if (k == 8 && game.getPlayerByNickName("Piero").getBoard().getEntryRoom().size() > 8) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|      " + shouldPrintStudent(k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "     " + shouldPrintStudent(++k, game.getPlayerByNickName("Piero").getBoard(), "██ ") + "      |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            i++;
                        } else if (k >= 7 && game.getPlayerByNickName("Piero").getBoard().getEntryRoom().size() <= 8) {
                            System.out.println("|                       |                                                                          |       |");
                            System.out.print("|                       |");
                            printStudentInsideDiningRoom(color[color_index], game.getPlayerByNickName("Piero").getBoard().getDiningRoom().get(color[color_index]), game.getPlayerByNickName("Piero").getBoard().getTeacher(color[color_index]));
                            System.out.println();
                            i++;
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
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
            } else if (i % 3 == 0 && i > numOfColor + 1)
                System.out.print("   ?   ");
            else if (i % 3 == 0 && i < numOfColor + 1)
                System.out.print("   " + toColor(color, "⦾" + ANSI_RESET + "   |"));
            else if (i != 10)
                System.out.print("   -   ");
            else if (teacher)
                System.out.print("   -       |   " + toColor(color, "●" + ANSI_RESET + "   |"));
            else
                System.out.print("   -       |   -   |");
        }
    }

    public String shouldPrintStudent(int studentID, Board board, String string) {
        if (studentID < board.getEntryRoom().size()) {
            return toColor(board.getEntryRoom().get(studentID), string);
        } else return "-- ";
    }
}
