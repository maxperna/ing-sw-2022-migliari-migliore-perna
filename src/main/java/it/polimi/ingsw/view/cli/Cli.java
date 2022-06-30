package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Class containing all the methods used to generate a Command Line Interface (CLI)
 * @author Alessio Migliore
 */
public class Cli extends ViewSubject implements View {

    private Thread threadReader;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private boolean tutorial;

    public Cli() {
        tutorial = true;
    }

    /**
     * Thread-safe version of an input reader
     * @return a string read from input
     * @throws ExecutionException when the action fails
     */
    public String read() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new ThreadInputReader());
        threadReader = new Thread(futureTask);
        threadReader.start();

        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }


    /**
     * Method used to start the CLI, calls the methods used to create a connection with server
     */
    @Override
    public void start() {
        System.out.println("                                                       ");
        System.out.println("▀███▀▀▀███                                       ██   ██");
        System.out.println(" ██     ▀█                                       ██");
        System.out.println(" ██   █  ▀███▄███▀██▀   ▀██▀▄█▀██▄ ▀████████▄ ██████▀███  ▄██▀███");
        System.out.println(" ██████    ██▀ ▀▀  ██   ▄█ ██   ██   ██    ██   ██    ██  ██   ▀▀");
        System.out.println(" ██   █  ▄ ██       ██ ▄█   ▄█████   ██    ██   ██    ██  ▀█████▄");
        System.out.println(" ██     ▄█ ██        ███   ██   ██   ██    ██   ██    ██  █▄   ██");
        System.out.println("▄██████████████▄      ▄█    ▀████▀██▄████  ████▄ ▀████████▄██████▀");
        System.out.println("                     ▄█");
        System.out.println("                   ██▀");
        System.out.println("Welcome to Eryantis");
        connectionRequest();

    }

    /**
     * Method used to get from input port and address required to connect to the server, notifies the controller with received values
     */
    @Override
    public void connectionRequest() {
        HashMap<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "13000";
        String address = "0.0.0.0";
        String port = "13000";
        System.out.println("Choose the server address: [" + defaultAddress + "]");
        /*try {
            address = read();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        if (address.equals(""))
            serverInfo.put("address", defaultAddress);
        else
            serverInfo.put("address", address);

        System.out.println("Choose the server port: [" + defaultPort + "]");
        /*try {
            port = read();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        if (port == null)
            serverInfo.put("port", defaultPort);
        else
            serverInfo.put("port", port);


        System.out.println("\n\nWaiting for game creation...");

        notifyListener(list -> list.connectionRequest(serverInfo));

    }

    /**
     * Method used to print all the cards played in the previous turn
     * @param cardMap is a map where each card is associated to the player's nickname
     */
    @Override
    public void showLastUsedCard(Map<String, AssistantCard> cardMap) {
        for (String nickname : cardMap.keySet()) {
            printLastCard(cardMap.get(nickname), nickname);
        }
    }

    /**
     * Method used to get from input the player nickname
     */
    @Override
    public void askPlayerNickname() {
        String nickname = null;

        do {
            System.out.print("Insert your nickname here: ");
            try {
                nickname = read();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (nickname.equals("")) {
                System.out.println("Invalid input");
            }
        } while (nickname.equals(""));

        String finalNickname = nickname;
        this.notifyListener((list) -> list.sendNickname(finalNickname));
    }

    /**
     * Method called only on first player's interface to get the game parameters, like number of players and expert mode
     */
    @Override
    public void askGameParam() {
        int numOfPlayers = 0;
        boolean expert = false;
        String expertMode = "";
        boolean valid = false;

        do {
            System.out.print("Choose the number of players [2, 3, 4]: ");
            try {
                numOfPlayers = Integer.parseInt(read());
                if (numOfPlayers < 2 || numOfPlayers > 4)
                    System.out.println("Error. Invalid input. Write a valid number");
                else
                    valid = true;
            } catch (NumberFormatException | ExecutionException e) {
                System.out.println();
                System.out.println("Error. Invalid input");
            }
        } while (!valid);

        valid = false;
        do {
            System.out.print("Do you want to play in expert mode? [Y/N] ");
            try {
                expertMode = read().toUpperCase();
                if (!expertMode.equals("Y") && !expertMode.equals("N")) {
                    System.out.println();
                    System.out.println("Error. Invalid input");
                } else
                    valid = true;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (expertMode.equals("Y")) {
                expert = true;
            }
        } while (!valid);

        int finalNumOfPlayers = numOfPlayers;
        boolean finalExpert = expert;
        this.notifyListener((list) -> list.sendGameParam(finalNumOfPlayers, finalExpert));
    }

    /**
     * Method used to let player choose its deck and tower color based on a list of these parameters available
     *
     * @param remainingTowers list of remaining available towerColor
     * @param remainingDecks  list of remaining available deckType
     */
    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        int tower = 0;
        int deck = 0;
        boolean valid = false;
        do {
            System.out.println("Remaining tower: " + remainingTowers);
            System.out.println("Select tower by putting its position (1 to " + remainingTowers.size() + "): ");

            try {
                tower = Integer.parseInt(read()) - 1;
                if (tower >= remainingTowers.size() || tower < 0)
                    System.out.println("Error. Invalid input");
                else
                    valid = true;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println();
                System.out.println("Error. Invalid input");
            }
        } while (!valid);

        valid = false;
        do {
            System.out.println("Remaining deck: " + remainingDecks);
            System.out.println("Select deck by putting its position (1 to " + remainingDecks.size() + "): ");

            try {
                deck = Integer.parseInt(read()) - 1;
                if (deck >= remainingDecks.size() || deck < 0)
                    System.out.println("Error. Invalid input");
                else
                    valid = true;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println();
                System.out.println("Error. Invalid input");
            }
        } while (!valid);


        int finalTower = tower;
        int finalDeck = deck;
        this.notifyListener((list) -> list.chooseTowerColorAndDeck(remainingTowers.get(finalTower), remainingDecks.get(finalDeck)));
        clear();

    }

    /**
     * Method used to print the island list
     *
     * @param gameFieldMap map with the gameField
     */
    @Override
    public void showGameField(Map<Integer, IslandNode> gameFieldMap) {
        System.out.println();
        if (tutorial) {
            System.out.println("This is the game field: it shows the students set on every island, a yellow island indicates that mother nature is actually there,");
            System.out.println("the number indicates the towers set on that island (2 or more if it is a super island), and the letter indicates which color is the tower (or the towers); the X shows that the island is blocked (expert only)");
        }

        int row;
        int max_row = 0;
        int max_column = 0;
        int count_id = 0;
        int increments = 0;
        String previousColor = ANSI_RESET;

        if (gameFieldMap.size() > 8) {                                                                                  //basic implementation of the structure that the map will have
            while (max_column * max_row - max_column < gameFieldMap.size()) {
                if (max_column >= max_row)
                    max_row++;
                else
                    max_column++;
            }
        } else {
            while (max_column * max_row < gameFieldMap.size()) {
                if (max_column >= max_row)
                    max_row++;
                else
                    max_column++;
            }
        }

        ArrayList<Integer> index = new ArrayList<>();

        switch (gameFieldMap.size()) {                                                                                  //index will contain a differently sorted list of integers based on the numbers of available islands
            case 3: {
                index.add(1);
                index.add(2);
                index.add(4);
                index.add(3);
            }
            case 4: {
                index.add(1);
                index.add(2);
                index.add(4);
                index.add(3);
            }
            case 5: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(6);
                index.add(5);
                index.add(4);
            }
            case 6: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(6);
                index.add(5);
                index.add(4);
            }
            case 7: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(8);
                index.add(4);
                index.add(7);
                index.add(6);
                index.add(5);
            }
            case 8: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(8);
                index.add(4);
                index.add(7);
                index.add(6);
                index.add(5);
            }
            case 9: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(4);
                index.add(10);
                index.add(5);
                index.add(9);
                index.add(8);
                index.add(7);
                index.add(6);
            }
            case 10: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(4);
                index.add(12);
                index.add(5);
                index.add(11);
                index.add(6);
                index.add(10);
                index.add(9);
                index.add(8);
                index.add(7);
            }
            case 11: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(4);
                index.add(12);
                index.add(5);
                index.add(11);
                index.add(6);
                index.add(10);
                index.add(9);
                index.add(8);
                index.add(7);
            }
            case 12: {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(4);
                index.add(12);
                index.add(5);
                index.add(11);
                index.add(6);
                index.add(10);
                index.add(9);
                index.add(8);
                index.add(7);
            }
        }


        StringBuilder string = new StringBuilder();
        for (int column = 0; column < max_column; column++) {
            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                    }

                    string.append("             ____________   " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            " + ANSI_RESET);
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                        previousColor = ANSI_YELLOW;
                    }
                    string.append("            / " + ANSI_RED + intToString(gameFieldMap.get(index.get(count_id)).getColorInfluence(Color.RED)) + ANSI_RESET + "  " + ANSI_BLUE + intToString(gameFieldMap.get(index.get(count_id)).getColorInfluence(Color.BLUE)) + ANSI_RESET + "  " + ANSI_YELLOW + intToString(gameFieldMap.get(index.get(count_id)).getColorInfluence(Color.YELLOW)) + previousColor + " \\  " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            ");
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                        previousColor = ANSI_YELLOW;
                    }
                    string.append("           /   " + ANSI_PINK + intToString(gameFieldMap.get(index.get(count_id)).getColorInfluence(Color.PINK)) + ANSI_RESET + "    " + ANSI_GREEN + intToString(gameFieldMap.get(index.get(count_id)).getColorInfluence(Color.GREEN)) + previousColor + "   \\ " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            ");
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                        previousColor = ANSI_YELLOW;
                    }
                    string.append("          /  " + previousColor + gameFieldMap.get(index.get(count_id)).getNumberOfTowers() + " TOWER(S)");
                    if (gameFieldMap.get(index.get(count_id)).getNumberOfTowers() > 0) {
                        switch (gameFieldMap.get(index.get(count_id)).getTowerColor()) {
                            case BLACK:
                                string.append(" B  \\" + ANSI_RESET);
                                break;
                            case WHITE:
                                string.append(" W  \\" + ANSI_RESET);
                                break;
                            case GRAY:
                                string.append(" G  \\" + ANSI_RESET);
                                break;
                        }
                    } else
                        string.append("    \\" + ANSI_RESET);

                    count_id++;
                    increments++;
                } else {
                    string.append("                            ");
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                        previousColor = ANSI_YELLOW;
                    }
                    if (gameFieldMap.get(index.get(count_id)).isStopped())
                        string.append("          \\       " + ANSI_RESET + "XX" + previousColor + "       /" + ANSI_RESET);
                    else
                        string.append("          \\                /" + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            " + ANSI_RESET);
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                    }
                    string.append("           \\              / " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            " + ANSI_RESET);
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                    }
                    if (index.get(count_id) > 9)
                        string.append("            \\ ISLAND  " + index.get(count_id) + " /  " + ANSI_RESET);
                    else
                        string.append("            \\  ISLAND  " + index.get(count_id) + " /  " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            " + ANSI_RESET);
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            count_id = count_id - increments;
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());


            for (row = 0; row < max_row; row++) {
                if ((column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) && index.get(count_id) <= gameFieldMap.size()) {
                    if (gameFieldMap.get(index.get(count_id)).checkMotherNature()) {
                        string.append(ANSI_YELLOW);
                    }
                    string.append("             ------------   " + ANSI_RESET);
                    count_id++;
                    increments++;
                } else {
                    string.append("                            " + ANSI_RESET);
                    if (column == 0 || row == 0 || row == max_row - 1 || column == max_column - 1) {
                        count_id++;
                        increments++;
                    }
                }
                previousColor = ANSI_RESET;
            }
            increments = 0;
            System.out.println(string);
            string.delete(0, string.capacity());
            System.out.println();
        }
    }

    /**
     * Method used to print the students inside all the cloud tiles
     *
     * @param newClouds arrayList that contains all the clouds in the game
     */
    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        for (CloudTile cloud : newClouds) {
            System.out.print("Cloud " + (cloud.getTileID() + 1) + " contains the following students: ");
            for (int j = 0; j < cloud.getStudents().size(); j++) {
                System.out.print(toColor(cloud.getStudents().get(j), "● "));
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Method used to print info about the active player and the gamePhase
     * @param currentPlayer nickName of the player that will play
     * @param currentState is the state of the game
     */
    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {
        System.out.println("Active player: " + currentPlayer + "\nPhase: " + currentState.toString());
    }

    /**
     * Method used to print a generic message received from the server
     * @param genericMessage contains message
     */
    public void showGenericMessage(String genericMessage) {
        System.out.println(genericMessage);
        System.out.println();
    }

    /**
     * Method used to print the number of coins owned by the player
     * @param player    is the player that modified his number of coins
     * @param numOfCoin is the new value of coin of the player
     */
    public void newCoin(String player, int numOfCoin) {
        System.out.println("Number of coins: " + numOfCoin);
        System.out.println();
    }

    /**
     * Method used to print the winning message
     *
     * @param winner nickname of the winner
     */
    @Override
    public void showWinner(String winner) {
        System.out.println("Congratulations! " + winner + " is the winner!");
    }

    /**
     * Method used to print an error message
     * @param errorMessage is the error message
     * @param errorType is the type of error
     */
    @Override
    public void showError(String errorMessage, ErrorType errorType) {
        System.out.println(errorMessage);
    }


    /**
     * Method used to print the description of each expert card
     */
    @Override
    public void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins) {
        int index = 1;

        for (ExpertCard expert : allExpertCards) {
            if (expert instanceof Expert1) {
                printStudentsInsideExpertCard((Expert1) expert, index);
                index++;
                System.out.println();
            } else if (expert instanceof Expert7) {
                printStudentsInsideExpertCard((Expert7) expert, index);
                index++;
                System.out.println();
            } else if (expert instanceof Expert11) {
                printStudentsInsideExpertCard((Expert11) expert, index);
                index++;
                System.out.println();
            } else {
                System.out.println("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription());
                index++;
                System.out.println();
            }
        }
        System.out.println("Number of coins available: " + numberOfCoins);
        System.out.println();
    }

    /**
     * Method used to print a list of students, get from input the chosen student and call on the ClientController moveStudentToDinner() or moveStudentToIsland()
     * @param availableStudents is a list of students (the entry hall students)
     * @param movementType is the destination
     * @param gameFieldSize is the size of the island list
     */
    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        System.out.println("\n\nAvailable students: ");
        Map<Integer, Color> indexColorMap = new HashMap<>();
        int islandChoice = -1;
        int choice = 0;
        int index = 1;

        for (Color currentColor : availableStudents) {
            if (currentColor.equals(Color.RED))
                System.out.println("[" + index + "] " + toColor(Color.RED, "●"));
            if (currentColor.equals(Color.PINK))
                System.out.println("[" + index + "] " + toColor(Color.PINK, "●"));
            if (currentColor.equals(Color.GREEN))
                System.out.println("[" + index + "] " + toColor(Color.GREEN, "●"));
            if (currentColor.equals(Color.YELLOW))
                System.out.println("[" + index + "] " + toColor(Color.YELLOW, "●"));
            if (currentColor.equals(Color.BLUE))
                System.out.println("[" + index + "] " + toColor(Color.BLUE, "●"));

            indexColorMap.put(index, currentColor);
            index++;
        }

        while (!indexColorMap.containsKey(choice)) {
            try {
                System.out.println("Choose: ");
                choice = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input");
            }
        }
        int finalChoice = choice;

        if (movementType == MessageType.MOVE_TO_DINING) {
            notifyListener(list -> list.moveStudentToDinner(indexColorMap.get(finalChoice)));
        } else if (movementType == MessageType.MOVE_TO_ISLAND) {

            do {
                try {
                    System.out.println("Island Number: ");
                    islandChoice = Integer.parseInt(read());
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (islandChoice <= 0 && islandChoice > gameFieldSize);

            int finalIslandChoice = islandChoice;
            notifyListener(list -> list.moveStudentToIsland(indexColorMap.get(finalChoice), finalIslandChoice));
        }
    }


    public void disconnect() {
    }

    /**
     * Method used to print the available AssistantCards
     * @param deck is the player's deck
     */
    @Override
    public void showAssistant(ArrayList<AssistantCard> deck) {
        System.out.println();
        int choice = 0;
        boolean valid = false;
        if (tutorial) {
            System.out.println("This is your card deck. On each card, the red number is the card's action number, the white number is the maximum number of moves that Mother Nature can perform");
            tutorial = false;
        }
        StringBuilder string = new StringBuilder();
        int index;
        for (index = 0; index < deck.size(); index++)
            string.append("__________     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index = 0; index < deck.size(); index++) {
            if (deck.get(index).getActionNumber() < 10)
                string.append("| " + ANSI_RED + deck.get(index).getActionNumber() + ANSI_RESET + "    " + (int) Math.ceil((float) deck.get(index).getActionNumber() / 2) + " |     ");
            else
                string.append("| " + ANSI_RED + deck.get(index).getActionNumber() + ANSI_RESET + "   " + (int) Math.ceil((float) deck.get(index).getActionNumber() / 2) + " |     ");
        }

        System.out.println(string);
        string.delete(0, string.capacity());

        for (index = 0; index < deck.size(); index++)
            string.append("|        |     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index = 0; index < deck.size(); index++)
            string.append("|        |     ");
        System.out.println(string);
        string.delete(0, string.capacity());

        for (index = 0; index < deck.size(); index++) {
            if (deck.get(index).getActionNumber() < 10)
                string.append("| " + ANSI_RED + deck.get(index).getActionNumber() + ANSI_RESET + "    " + (int) Math.ceil((float) deck.get(index).getActionNumber() / 2) + " |     ");
            else
                string.append("| " + ANSI_RED + deck.get(index).getActionNumber() + ANSI_RESET + "   " + (int) Math.ceil((float) deck.get(index).getActionNumber() / 2) + " |     ");
        }
        System.out.println(string);
        string.delete(0, string.capacity());

        for (index = 0; index < deck.size(); index++)
            string.append("----------     ");
        System.out.println(string);
        string.delete(0, string.capacity());

        System.out.println("Choose your card by writing its action number: the lower it is, the higher the chances for you to start the action phase (based on other players' cards)");
        do {
            try {
                choice = Integer.parseInt(read());
                for (AssistantCard card : deck) {
                    if (card.getActionNumber() == choice) {
                        valid = true;
                        break;
                    }
                }
                if (!valid)
                    System.out.println("Error. Card not found");
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input");
            }
        } while (!valid);

        int finalChoice = choice;
        this.notifyListener((list) -> list.playAssistantCard(finalChoice));
    }

    /**
     * Method used to print a player's board
     * @param board is the board that will be printed
     * @param player is the board's owner
     */
    @Override
    public void printBoard(Board board, String player) {

        System.out.println("This is " + player + "'s board");
        System.out.println("Tower color: " + board.getTowerColor());
        System.out.println("Number of towers available: " + board.getNumOfTowers());
        System.out.println();

        int studentIndex = 0;
        int hallDimension = board.getMaxStudentHall();
        Color[] color = Color.values();
        int color_index = 0;

        for (int i = 0; i < 6; i++) {
            for (int column = 0; column < 108; column++) {
                if (i == 0 || i == 5)
                    System.out.print("_");
                else if (i == 1 && column == 0)
                    System.out.println();
                else if (column == 0 || column == 6) {
                    if (studentIndex < hallDimension - 1) {
                        System.out.println("|                       |                                                                          |       |");
                        System.out.print("|      " + shouldPrintStudent(studentIndex, board, "██ ") + "     " + shouldPrintStudent(++studentIndex, board, "██ ") + "      |");
                        printStudentInsideDiningRoom(color[color_index], board.getDiningRoom().get(color[color_index]), board.getTeacher(color[color_index]));
                        color_index++;
                        System.out.println();
                        studentIndex++;
                    } else if (studentIndex == hallDimension - 1 && hallDimension == 7) {
                        System.out.println("|                       |                                                                          |       |");
                        System.out.print("|      " + shouldPrintStudent(studentIndex, board, "██ ") + "     " + shouldPrintStudent(++studentIndex, board, "██ ") + "      |");
                        printStudentInsideDiningRoom(color[color_index], board.getDiningRoom().get(color[color_index]), board.getTeacher(color[color_index]));
                        color_index++;
                        System.out.println();
                        System.out.println("|                       |                                                                          |       |");
                        System.out.print("|                       |");
                        printStudentInsideDiningRoom(color[color_index], board.getDiningRoom().get(color[color_index]), board.getTeacher(color[color_index]));
                        System.out.println();
                        studentIndex++;
                    } else if (studentIndex == hallDimension - 1) {
                        System.out.println("|                       |                                                                          |       |");
                        System.out.print("|      " + shouldPrintStudent(studentIndex, board, "██ ") + "     " + shouldPrintStudent(++studentIndex, board, "██ ") + "      |");
                        printStudentInsideDiningRoom(color[color_index], board.getDiningRoom().get(color[color_index]), board.getTeacher(color[color_index]));
                        System.out.println();
                        i++;
                    }
                }
            }
        }
        System.out.println();
    }

    /**
     * Method used to print a given string with a given color
     *
     * @param color  is the color of the string that will be printed
     * @param string is the string that will be printed
     * @return a combination of colors and the string that will be printed, empty string in the default case
     */
    private String toColor(Color color, String string) {
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

    /**
     * Method called by the method showBoard()
     *
     * @param color      is the color of the row we are considering
     * @param numOfColor is the number of students of that given color that are set on the dining room
     * @param teacher    is a boolean that indicates if the board passed to the showBoard() method contains the teacher of the given color
     */
    private void printStudentInsideDiningRoom(Color color, int numOfColor, Boolean teacher) {

        for (int i = 1; i < 11; i++) {
            if (i < numOfColor + 1) {
                System.out.print("   " + toColor(color, "●") + "   ");
            } else if (i % 3 == 0 && i > numOfColor)
                System.out.print("   ?   ");
            else if (i % 3 == 0 && i < numOfColor)
                System.out.print("   " + toColor(color, "⦾" + ANSI_RESET + "   |"));
            else if (i != 10)
                System.out.print("   -   ");
            else if (teacher)
                System.out.print("   -       |   " + toColor(color, "●" + ANSI_RESET + "   |"));
            else
                System.out.print("   -       |   -   |");
        }
    }

    /**
     * Method used to get from the user the board he wants to check
     *
     * @param boardMap is a map of all the available boards in the game
     */
    @Override
    public void showBoard(Map<String, Board> boardMap) {
        int index = 0;
        Map<Integer, String> integerToString = new HashMap<>();
        int chosenPlayer = 0;
        boolean valid = false;
        do {
            try {

                index = 0;
                System.out.println("Choose which board you want to check: ");
                for (String s : boardMap.keySet()) {
                    System.out.println("[" + index + "] for " + s);
                    integerToString.put(index, s);
                    index++;
                }
                System.out.println("[" + index + "] for other boards");
                chosenPlayer = Integer.parseInt(read());

                if ((chosenPlayer < 0 || chosenPlayer > boardMap.size() + 1))
                    System.out.println("Error. Invalid input");
                else
                    valid = true;

            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input");
            }

        } while (!valid);

        if (chosenPlayer == index) {
            for (String s : boardMap.keySet()) {
                printBoard(boardMap.get(s), s);
            }

        } else {
            printBoard(boardMap.get(integerToString.get(chosenPlayer)), integerToString.get(chosenPlayer));
        }


    }

    /**
     * Method that checks what to print inside the entry hall
     * @param studentID is the student position inside the entry hall
     * @param board is the player's board
     * @param string is the symbol used to print a student
     * @return a string colored or "--"
     */
    private String shouldPrintStudent(int studentID, Board board, String string) {
        if (studentID < board.getEntryRoom().size()) {
            return toColor(board.getEntryRoom().get(studentID), string);
        } else return "-- ";
    }

    /**
     * Method used to manage the actionPhaseTurn
     * @param expert is a boolean that is used to eventually play the expert card
     */
    @Override
    public void actionPhaseTurn(Boolean expert) {
        int choice = 0;
        boolean valid = false;

        if (expert) {
            do {
                System.out.println("\nWrite [1] to move a student to the dining room, [2] to move it on an island, [3] to play an expert card");
                try {
                    choice = Integer.parseInt(read());

                    if (choice == 1 || choice == 2 || choice == 3)
                        valid = true;

                } catch (NumberFormatException | ExecutionException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (!valid);

            if (choice == 1)
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_DINING));
            else if (choice == 2) {
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_ISLAND));
            } else {
                notifyListener(list -> list.actionPhaseChoice(MessageType.EXPERT_CARD_REQ));
            }
        } else {
            do {
                System.out.println("\nWrite [1] to move a student to the dining room, [2] to move it on an island");
                try {
                    choice = Integer.parseInt(read());

                    if (choice == 1 || choice == 2)
                        valid = true;

                } catch (NumberFormatException | ExecutionException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (!valid);

            if (choice == 1)
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_DINING));
            else {
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_ISLAND));
            }
        }
    }

    /**
     * Method used to ask the player the number of moves that mother nature will perform
     */
    @Override
    public void moveMotherNature() {
        System.out.println("Select the number of moves for Mother Nature: ");

        try {
            int num = Integer.parseInt(read());
            this.notifyListener((list) -> list.moveMotherNature(num));
        } catch (ExecutionException | NumberFormatException e) {
            System.out.println("Error. Invalid input");
        }

    }

    /**
     * Methode used to ask the player which cloudTile he wants to pick the students from
     * @param cloudID is an int that identifies the cloud [0,1,2]
     */
    public void chooseCloudTile(int cloudID) {
        int chosenCloud = 0;
        boolean valid = false;
        do {
            try {

                System.out.print("Choose the cloud you want to get the students from: ");
                for (int i = 0; i < cloudID; i++) {
                    System.out.print("[" + (i + 1) + "]");
                }
                System.out.println();
                chosenCloud = Integer.parseInt(read()) - 1;

                if ((chosenCloud < 0 || chosenCloud > cloudID))
                    System.out.println("Invalid parameter");
                else
                    valid = true;

            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }

        } while (!valid);

        int finalChosenCloud = chosenCloud;
        notifyListener(list -> list.chooseCloudTile(finalChosenCloud));
    }

    @Override
    public void startGame() {
        System.out.println("GAME STARTED!!");
    }

    @Override
    public void availableAction(boolean allStudentsMoved, boolean motherNatureMoved, boolean expertPlayed) {

    }

    /**
     * Method used to generate the default layout
     * @param gameFieldMap is a map containing the islands
     * @param chargedClouds is an arraylist containing all the infos about cloudTiles
     * @param boardMap is a map of all the boards
     * @param currentPlayer is the nickname of the actual player
     * @param experts is an arraylist of expert cards
     * @param numOfCoins is the number of coins owned by the player
     */
    @Override
    public void worldUpdate(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap,String nick, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        clear();
        showGameField(gameFieldMap);
        showClouds(chargedClouds);
        printBoard(boardMap.get(nick), nick);
        showExpertCards(experts, numOfCoins);
    }


    /**
     * Method used to print all the int numbers from 0 to 99 with 2 numbers, to get a coherent graphic inside the island node
     * @param number is the int that will be converted into a string
     * @return a string representation of the number, that will be printed with a different color
     */
    private String intToString(Integer number) {
        if (number < 10)
            return 0 + number.toString();
        else
            return number.toString();
    }

    /**
     * Method called by lastUsedCards() to print a card matching its deck color
     * @param card is the card that will be played
     * @param nickname is the card's owner
     */
    public void printLastCard(AssistantCard card, String nickname) {
        StringBuilder string = new StringBuilder();
        String cardColor = "";
        DeckType deck_type = card.getDeckType();

        switch (deck_type) {
            case SAGE:
                string.append(ANSI_BLUE);
                break;
            case DRUID:
                string.append(ANSI_GREEN);
                break;
            case KING:
                string.append(ANSI_YELLOW);
                break;
            case WITCH:
                string.append(ANSI_PINK);
                break;
        }

        System.out.println("This is " + nickname + "'s last used card");
        string.append("_".repeat(10));

        System.out.println(cardColor + string);
        string.delete(0, string.capacity());

        if (card.getActionNumber() < 10)
            string.append("| " + card.getActionNumber() + "    " + (int) Math.ceil((float) card.getActionNumber() / 2) + " |     ");
        else
            string.append("| " + card.getActionNumber() + "   " + (int) Math.ceil((float) card.getActionNumber() / 2) + " |     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        System.out.println("|        |");
        System.out.println("|        |");
        if (card.getActionNumber() < 10)
            string.append("| " + card.getActionNumber() + "    " + (int) Math.ceil((float) card.getActionNumber() / 2) + " |     ");
        else
            string.append("| " + card.getActionNumber() + "   " + (int) Math.ceil((float) card.getActionNumber() / 2) + " |     ");
        System.out.println(string);
        System.out.println("----------" + ANSI_RESET);
        string.delete(0, string.capacity());
    }

    /**
     * method to play expert card 9
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType2(int cardID, Expert9 expert) {
        int chosenColor = 0;
        System.out.println("Choose a color: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
        do {
            try {
                chosenColor = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error.Invalid input");
            }
        } while (chosenColor < 1 || chosenColor > 5);

        switch (chosenColor) {
            case 1:
                notifyListener(list -> list.playExpertCard4(cardID, Color.RED));
                break;
            case 2:
                notifyListener(list -> list.playExpertCard4(cardID, Color.PINK));
                break;
            case 3:
                notifyListener(list -> list.playExpertCard4(cardID, Color.GREEN));
                break;
            case 4:
                notifyListener(list -> list.playExpertCard4(cardID, Color.YELLOW));
                break;
            case 5:
                notifyListener(list -> list.playExpertCard4(cardID, Color.BLUE));
                break;

        }
    }

    /**
     * method to play expert card 11
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType2(int cardID, Expert11 expert) {
        int index;
        int chosenStudent = 0;
        do {
            index = 1;
            System.out.println("Choose one of the students on this card:");
            for (Color student : expert.getStudentsOnCard()) {
                System.out.println("[" + index + "] " + toColor(student, "●"));
                index++;
            }
            try {
                chosenStudent = Integer.parseInt(read()) - 1;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while (chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size() - 1);

        int finalChosenStudent = chosenStudent;
        notifyListener(list -> list.playExpertCard4(cardID, expert.getStudentsOnCard().get(finalChosenStudent)));
    }

    /**
     * method to play expert card 12
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType2(int cardID, Expert12 expert) {
        int chosenColor = 0;
        System.out.println("Choose a color: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
        do {
            try {
                chosenColor = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error.Invalid input");
            }
        } while (chosenColor < 1 || chosenColor > 5);

        switch (chosenColor) {
            case 1:
                notifyListener(list -> list.playExpertCard4(cardID, Color.RED));
                break;
            case 2:
                notifyListener(list -> list.playExpertCard4(cardID, Color.PINK));
                break;
            case 3:
                notifyListener(list -> list.playExpertCard4(cardID, Color.GREEN));
                break;
            case 4:
                notifyListener(list -> list.playExpertCard4(cardID, Color.YELLOW));
                break;
            case 5:
                notifyListener(list -> list.playExpertCard4(cardID, Color.BLUE));
                break;
        }
    }

    /**
     * method to play expert card 7
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType3(int cardID, Expert7 expert) {
        ArrayList<Color> toCard = new ArrayList<>();
        ArrayList<Color> toEntryHall = new ArrayList<>();
        int chosenColor = 0;
        int maxStudent = 0;

        do {
            System.out.print("Choose the number of students you want to swap (up to 3): ");
            try {
                maxStudent = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error.Invalid parameter");
            }
        } while (maxStudent < 1 || maxStudent > 3);

        for (int i = 0; i < maxStudent; i++) {
            int index;
            int chosenStudent = 0;

            do {
                index = 1;
                System.out.println("Choose the student you want to move to your entry hall");
                for (Color student : expert.getStudentsOnCard()) {
                    System.out.println("[" + index + "] " + toColor(student, "●"));
                    index++;
                }
                try {
                    chosenStudent = Integer.parseInt(read()) - 1;
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size() - 1);

            toEntryHall.add(expert.getStudentsOnCard().get(chosenStudent));
            expert.getStudentsOnCard().remove(chosenStudent);
        }

        for (int i = 0; i < maxStudent; i++) {
            do {
                System.out.println("Choose the student you want to place on the card: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
                try {
                    chosenColor = Integer.parseInt(read());
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (chosenColor < 1 || chosenColor > 5);

            switch (chosenColor) {
                case 1:
                    toCard.add(Color.RED);
                    break;
                case 2:
                    toCard.add(Color.PINK);
                    break;
                case 3:
                    toCard.add(Color.GREEN);
                    break;
                case 4:
                    toCard.add(Color.YELLOW);
                    break;
                case 5:
                    toCard.add(Color.BLUE);
                    break;
            }
        }
        notifyListener(list -> list.playExpertCard5(cardID, toEntryHall, toCard));
    }

    /**
     * method to play expert card 10
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType3(int cardID, Expert10 expert) {
        ArrayList<Color> toDiningRoom = new ArrayList<>();
        ArrayList<Color> toEntryHall = new ArrayList<>();
        int chosenColor = 0;
        int maxStudent = 0;
        do {
            System.out.print("Choose the number of student you want to swap (up to 2): ");
            try {
                maxStudent = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error.Invalid input");
            }
        } while (maxStudent < 1 || maxStudent > 2);

        for (int i = 0; i < maxStudent; i++) {
            do {
                System.out.println("Choose the student you want to move from your hall to your dining room: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
                try {
                    chosenColor = Integer.parseInt(read());
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (chosenColor < 1 || chosenColor > 5);

            switch (chosenColor) {
                case 1:
                    toDiningRoom.add(Color.RED);
                    break;
                case 2:
                    toDiningRoom.add(Color.PINK);
                    break;
                case 3:
                    toDiningRoom.add(Color.GREEN);
                    break;
                case 4:
                    toDiningRoom.add(Color.YELLOW);
                    break;
                case 5:
                    toDiningRoom.add(Color.BLUE);
                    break;

            }
        }

        for (int i = 0; i < maxStudent; i++) {
            do {
                System.out.println("Choose the student you want to move from your dining room to your entry hall: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
                try {
                    chosenColor = Integer.parseInt(read());
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while (chosenColor < 1 || chosenColor > 5);

            switch (chosenColor) {
                case 1:
                    toEntryHall.add(Color.RED);
                    break;
                case 2:
                    toEntryHall.add(Color.PINK);
                    break;
                case 3:
                    toEntryHall.add(Color.GREEN);
                    break;
                case 4:
                    toEntryHall.add(Color.YELLOW);
                    break;
                case 5:
                    toEntryHall.add(Color.BLUE);
                    break;
            }
        }
        notifyListener(list -> list.playExpertCard5(cardID, toDiningRoom, toEntryHall));
    }

    /**
     * method to play expert card 3
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType4(int cardID, Expert3 expert) {
        int chosenID = 0;
        System.out.println("Choose the island you want to use the effect on: ");
        do {
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while (chosenID < 0 || chosenID > 12);


        int finalChosenID = chosenID;
        notifyListener(list -> list.playExpertCard2(cardID, finalChosenID));
    }

    /**
     * method to play expert card 1
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    @Override
    public void playExpertType5(int cardID, Expert1 expert) {
        int index;
        int chosenStudent = 0;

        do {
            index = 1;
            System.out.println("Choose one of the students on this card:");
            for (Color student : expert.getStudentsOnCard()) {
                System.out.println("[" + index + "] " + toColor(student, "●"));

                index++;
            }
            try {
                chosenStudent = Integer.parseInt(read()) - 1;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while (chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size() - 1);

        int chosenID = 0;

        do {
            System.out.println("Choose the island you want to use the effect on: ");
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while (chosenID < 0 || chosenID > 12);
        int finalChosenID = chosenID;
        int finalChosenStudent = chosenStudent;
        notifyListener(list -> list.playExpertCard3(cardID, finalChosenID, expert.getStudentsOnCard().get(finalChosenStudent)));
    }

    /**
     * method to play expert card 5
     *
     * @param cardID is the value [0,1,2] used by clientController to identify which expert card is being played
     * @param expert is the expert card that is being played
     */
    public void playExpertType5(int cardID, Expert5 expert) {

        int chosenID = 0;

        do {
            System.out.println("Choose the island you want to use the effect on: ");
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while (chosenID < 0 || chosenID > 12);
        int finalChosenID = chosenID;
        notifyListener(list -> list.playExpertCard2(cardID, finalChosenID));
    }

    /**
     * Method used to ask the player what to do during preparation phase
     */
    @Override
    public void chooseAction(boolean expertMode) {
        int chosenAction = 0;
        boolean valid = false;
        System.out.println("Write [1] to play your card, [2] to check the other boards");
        do {
            try {
                chosenAction = Integer.parseInt(read());
                if (chosenAction != 1 && chosenAction != 2)
                    System.out.println("Invalid input");
                else
                    valid = true;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input.");
            }
        } while (!valid);
        int finalChosenAction = chosenAction;
        notifyListener(list -> list.chooseAction(finalChosenAction));
    }

    /**
     * Method used to choose the expert
     */
    @Override
    public void chooseExpertCard() {
        int chosenExpert = 0;
        boolean valid = true;
        do {
            System.out.println("Write the ID of the Expert card you want to play: [0] to return");
            try {
                chosenExpert = Integer.parseInt(read()) - 1;
            } catch (ExecutionException | NumberFormatException e) {
                valid = false;
            }
            if (chosenExpert < -1 || chosenExpert > 2)
                valid = false;
            if (!valid)
                System.out.println("Error. Invalid parameter");
        } while (chosenExpert < -1 || chosenExpert > 2);

        if (chosenExpert != -1) {
            int finalChosenExpert = chosenExpert;
            notifyListener(list -> list.applyExpertEffect(finalChosenExpert));
        } else
            notifyListener(list -> list.askAction(true));

    }


    /**
     * Method used to ask if the player wants to move mother nature and end its turn, or play an expert card
     */
    public void playExpertChoice() {
        int choice = 0;

        do {
            System.out.println("Write [1] to move Mother Nature, [2] to play an Expert");
            try {
                choice = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input");
            }
        }
        while (!(choice == 1 || choice == 2));

        final int finalChoice = choice;

        if (finalChoice == 1) //49 is ascii number for 1
            moveMotherNature();
        else notifyListener(list -> list.actionPhaseChoice(MessageType.EXPERT_CARD_REQ));
    }

    @Override
    public void clear() {
        try {
            String operatingSystem = System.getProperty("os.name");

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to print the students placed on expert card 1
     * @param expert is the expert card
     * @param index is the student's position
     */
    private void printStudentsInsideExpertCard(Expert1 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription() + "\nStudents on this card: ");
        for (Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student, "● "));
        }
        System.out.println();
    }

    /**
     * Method used to print the students placed on expert card 7
     * @param expert is the expert card
     * @param index is the student's position
     */
    private void printStudentsInsideExpertCard(Expert7 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription() + "\nStudents on this card: ");
        for (Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student, "● "));
        }
        System.out.println();
    }

    /**
     * Method used to print the students placed on expert card 11
     * @param expert is the expert card
     * @param index is the student's position
     */
    private void printStudentsInsideExpertCard(Expert11 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription() + "\nStudents on this card: ");
        for (Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student, "● "));
        }
        System.out.println();
    }
}
