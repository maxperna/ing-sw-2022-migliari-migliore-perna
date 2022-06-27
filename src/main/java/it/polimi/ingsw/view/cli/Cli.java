package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.*;
import it.polimi.ingsw.model.gameField.IsladNode;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.View;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewSubject implements View {

    private Thread threadReader;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String CLEAR = "\033[H\033[2J";
    private boolean tutorial;

    public Cli() {
        tutorial = true;
    }

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
     * method used to start the CLI, calls the methods used to create a connection with server and to ask for player nickname
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
     * method used to get from input port and address required to connect to the server, notifies the controller with received values
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

    @Override
    public void showLastUsedCard(Map<String, AssistantCard> cardMap) {
        for (String nickname : cardMap.keySet()) {
            printLastCard(cardMap.get(nickname), nickname);
        }
    }

    /**
     * method used to get from input the player nickname
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
        this.notifyListener((list) -> {
            list.sendNickname(finalNickname);
        });
    }

    /**
     * method called only on first player's interface to get the game parameters, like number of players and expert mode
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
                valid = false;
            }
        } while (!valid);

        valid = false;
        do{
            System.out.print("Do you want to play in expert mode? [Y/N] ");
            try {
                expertMode = read().toUpperCase();
                if (!expertMode.equals("Y")  && !expertMode.equals("N")) {
                    System.out.println();
                    System.out.println("Error. Invalid input");
                }
                else
                    valid = true;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (expertMode.equals("Y")) {
                expert = true;
            }
        } while(!valid);

        int finalNumOfPlayers = numOfPlayers;
        boolean finalExpert = expert;
        this.notifyListener((list) -> {
            list.sendGameParam(finalNumOfPlayers, finalExpert);
        });
    }

    /**
     * method used to let player choose its deck and tower color based on a list of this parameters available
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
                valid = false;
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
                valid = false;
            }
        } while (!valid);


        int finalTower = tower;
        int finalDeck = deck;
        this.notifyListener((list) -> {
            list.chooseTowerColorAndDeck(remainingTowers.get(finalTower), remainingDecks.get(finalDeck));
        });
        clearCli();

    }

    @Override
    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {

    }

    /**
     * method used to print the island list and then to call the chooseBoard() method
     *
     * @param gameFieldMap map with the gameField
     */
    @Override
    public void showGameField(Map<Integer, IsladNode> gameFieldMap) {
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

        if (gameFieldMap.size() > 8) {
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

        switch (gameFieldMap.size()) {
            case 3 : {
                index.add(1);
                index.add(2);
                index.add(4);
                index.add(3);
            }
            case 4 : {
                index.add(1);
                index.add(2);
                index.add(4);
                index.add(3);
            }
            case 5 : {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(6);
                index.add(5);
                index.add(4);
            }
            case 6 : {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(6);
                index.add(5);
                index.add(4);
            }
            case 7 : {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(8);
                index.add(4);
                index.add(7);
                index.add(6);
                index.add(5);
            }
            case 8 : {
                index.add(1);
                index.add(2);
                index.add(3);
                index.add(8);
                index.add(4);
                index.add(7);
                index.add(6);
                index.add(5);
            }
            case 9 : {
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
            case 10 : {
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
            case 11 : {
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
            case 12 : {
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
     * method used to print the students inside all the cloud tiles
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

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {

    }


    public void updateTeachers(Map<Color, Boolean> teacherList) {
    }

    public void updateNode(IsladNode updatedNode) {
        System.out.println("A change happened on an island");
    }

    public void showGenericMessage(String genericMessage) {
        System.out.println(genericMessage);
        System.out.println();
    }

    public void newCoin(String player, int numOfCoin) {
        System.out.println("Number of coins: " +numOfCoin);
        System.out.println();
    }

    /**
     * method used to print the winning message
     *
     * @param winner nickname of the winner
     */
    @Override
    public void showWinner(String winner) {
        System.out.println("Congratulations! " + winner + " is the winner!");
    }

    @Override
    public void showError(String errorMessage, ErrorType errorType) {
        System.out.println(errorMessage);
    }

    @Override
    public void showExpertCards(ArrayList<ExpertCard> expertCard) {

    }


    /**
     * method used to print the description of each expert card
     *
     */
    @Override
    public void showExpertCards(ArrayList<ExpertCard> allExpertCards, int numberOfCoins) {
        int index = 1;

        for (ExpertCard expert : allExpertCards) {
            if (expert instanceof Expert1) {
                printStudentsInsideExpertCard((Expert1) expert, index);
                index++;
                System.out.println();
            }
            else if (expert instanceof Expert7) {
                printStudentsInsideExpertCard((Expert7) expert, index);
                index++;
                System.out.println();
            }
            else if (expert instanceof Expert11) {
                printStudentsInsideExpertCard((Expert11) expert, index);
                index++;
                System.out.println();
            }
            else{
                System.out.println("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription());
                index++;
                System.out.println();
            }
        }
        System.out.println("Number of coins available: " +numberOfCoins);
        System.out.println();
    }

    @Override
    public void availableStudents(ArrayList<Color> availableStudents, MessageType movementType, int gameFieldSize) {
        System.out.println("\n\nAvailable students: ");
        Map<Integer, Color> indexColorMap = new HashMap<>();
        int islandChoice = -1;
        int choice = 0;
        int index = 1;

        for(Color currentColor : availableStudents) {
            if(currentColor.equals(Color.RED))
                System.out.println("[" +index+ "] "+toColor(Color.RED, "●"));
            if(currentColor.equals(Color.PINK))
                System.out.println("[" +index+ "] "+toColor(Color.PINK, "●"));
            if(currentColor.equals(Color.GREEN))
                System.out.println("[" +index+ "] "+toColor(Color.GREEN, "●"));
            if(currentColor.equals(Color.YELLOW))
                System.out.println("[" +index+ "] "+toColor(Color.YELLOW, "●"));
            if(currentColor.equals(Color.BLUE))
                System.out.println("[" +index+ "] "+toColor(Color.BLUE, "●"));

            indexColorMap.put(index, currentColor);
            index ++;
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

        if(movementType == MessageType.MOVE_TO_DINING) {
            notifyListener(list -> list.moveStudentToDinner(indexColorMap.get(finalChoice)));
        }
        else if(movementType == MessageType.MOVE_TO_ISLAND) {

            do {
                try {
                    System.out.println("Island Number: ");
                    islandChoice = Integer.parseInt(read());
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            }while (islandChoice <= 0 && islandChoice > gameFieldSize);

            int finalIslandChoice = islandChoice;
            notifyListener(list -> list.moveStudentToIsland(indexColorMap.get(finalChoice), finalIslandChoice));
        }
    }

    /**
     * method used to get from input an ID from a list of given ones
     *
     * @param ID
     */
    public void sendSelectedID(ArrayList<Integer> ID) {
        int chosenID = 0;
        do {
            System.out.println("Choose one from the available objects: " + ID.toString());
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                e.printStackTrace();
            }
            if (!ID.contains(chosenID)) {
                System.out.println("Invalid input");
            }
        } while (!ID.contains(chosenID));

        int finalChosenID = chosenID;
        this.notifyListener((list) -> {
            list.chooseCloudTile(finalChosenID);
        });
    }

    /**
     * method used to get a student color from all the available ones and to get the destination
     *
     * @param students is the arraylist containing all the available students
     * @param islands  is an int that contains the id of the last island, basically indicating the number of islands remaining
     */
    @Override
    public void selectStudent(ArrayList<Color> students, int islands) {
        Color finalColor = null;
        int chosenIsland = 0;

        String color = null;
        do {
            System.out.println("Choose the color of the student you want to move: " + students.toString());
            try {
                color = read().toUpperCase(Locale.ROOT);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!color.contains("GREEN") && !color.contains("PINK") && !color.contains("RED") && !color.contains("YELLOW") && !color.contains("BLUE")) {
            }

            System.out.println("Invalid parameter");
        } while (!color.contains("GREEN") && !color.contains("PINK") && !color.contains("RED") && !color.contains("YELLOW") && !color.contains("BLUE"));

        switch (color.toUpperCase(Locale.ROOT)) {
            case "RED":
                finalColor = Color.RED;
                break;
            case "YELLOW":
                finalColor = Color.YELLOW;
                break;
            case "PINK":
                finalColor = Color.PINK;
                break;
            case "GREEN":
                finalColor = Color.GREEN;
                break;
            case "BLUE":
                finalColor = Color.BLUE;
                break;
        }
        Color finalColor1 = finalColor;
        String position = chooseDestination();
        if (position.contains("BOARD"))
            notifyListener(list -> list.moveStudentToDinner(finalColor1));
        else if (position.contains("ISLAND")) {
            System.out.println("On which island do you want to move the student?");
            try {
                chosenIsland = Integer.parseInt(read());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int finalChosenIsland = chosenIsland;
            notifyListener(list -> list.moveStudentToIsland(finalColor1, finalChosenIsland));
        }
    }

    /**
     * method called by selectStudent() to get the destination
     *
     * @return a string with the chosen destination
     */
    private String chooseDestination() {
        String destination = null;
        do {
            System.out.println("Choose the destination of the student: [BOARD], [ISLAND]");
            try {
                destination = read().toUpperCase(Locale.ROOT);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!destination.toUpperCase().equals("BOARD") && !destination.toUpperCase().equals("ISLAND")) {
                System.out.println("Invalid input");
            }
        } while (!destination.toUpperCase().equals("BOARD") && !destination.toUpperCase().equals("ISLAND"));

        return destination;
    }

    public void getPlayerInfo(ArrayList<String> players) {
        String player = null;
        do {
            System.out.println("Choose the player you want to get info on: " + players.toString());
            try {
                player = read();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (player.equals("") || !players.toString().contains(player)) {
                System.out.println("Invalid input");
            }
        } while (player.equals(""));

        String finalPlayer = player;
        this.notifyListener((list) -> {
            list.getPlayerInfo(finalPlayer);
        });
    }

    public void disconnect() {
    }

    @Override
    public void showAssistant(ArrayList<AssistantCard> deck) {
        System.out.println();
        int choice =0;
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
                    if (card.getActionNumber() == choice)
                        valid = true;
                }
                if(!valid)
                    System.out.println("Error. Card not found");
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input");
                valid = false;
            }
        } while(!valid);

        int finalChoice = choice;
        this.notifyListener((list) -> {
            list.playAssistantCard(finalChoice);
        });
    }

    public void clearCli() {
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
            System.out.println(e);
        }
    }

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
     * method used to print a given string with a given color
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
     * method called by the method showBoard()
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
     * method used to get from the user the board he wants to check
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
                valid = false;
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

    private String shouldPrintStudent(int studentID, Board board, String string) {
        if (studentID < board.getEntryRoom().size()) {
            return toColor(board.getEntryRoom().get(studentID), string);
        } else return "-- ";
    }

    @Override
    public void ActionPhaseTurn(Boolean expert) {
        int choice = 0;
        boolean valid = false;

        if(expert) {
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

            if(choice == 1)
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_DINING));
            else if (choice == 2){
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_ISLAND));
            }
            else {
                notifyListener(list -> list.actionPhaseChoice(MessageType.EXPERT_CARD_REQ));
            }
        }
        else {
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

            if(choice == 1)
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_DINING));
            else {
                notifyListener(list -> list.actionPhaseChoice(MessageType.MOVE_TO_ISLAND));
            }
        }
    }

    @Override
    public void ActionPhaseTurn() {

    }

    @Override
    public void moveMotherNature() {
        System.out.println("Select the number of moves for Mother Nature: ");

        try {
            int num = Integer.parseInt(read());
            this.notifyListener((list) -> {
                list.moveMotherNature(num);
            });
        } catch (ExecutionException | NumberFormatException e) {
            System.out.println("Error. Invalid input");
        }

    }


    public void chooseCloudTile(int cloudID) {
        int chosenCloud = 0;
        boolean valid = false;
        do {
            try {

                System.out.print("Choose the cloud you want to get the students from: ");
                for(int i = 0; i< cloudID; i++ ) {
                    System.out.print("["+(i+1)+"]");
                }
                System.out.println();
                chosenCloud = Integer.parseInt(read())-1;

                if((chosenCloud < 0 || chosenCloud > cloudID))
                    System.out.println("Invalid parameter");
                else
                    valid = true;

            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
                valid = false;
            }

        } while(!valid);

        int finalChosenCloud = chosenCloud;
        notifyListener(list -> list.chooseCloudTile(finalChosenCloud));
    }

    @Override
    public void sendNumberOfPlayers(int numberOfPlayers) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void worldUpdate(Map<Integer, IsladNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Map<String, Board> boardMap, String currentPlayer, ArrayList<ExpertCard> experts, int numOfCoins) {
        clear();
        showGameField(gameFieldMap);
        showClouds(chargedClouds);
        printBoard(boardMap.get(currentPlayer), currentPlayer);
        showExpertCards(experts, numOfCoins);
    }

    @Override
    public void startGame() {
        System.out.println("GAME STARTED!!");
    }

    private String intToString (Integer number) {
        if (number < 10)
            return 0+number.toString();
        else
            return number.toString();
    }

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
        System.out.println("----------"+ANSI_RESET);
        string.delete(0, string.capacity());
        }

    @Override
    public void playExpertType2(int cardID, Expert9 expert) {
        int chosenColor=0;
        System.out.println("Choose a color: [1] RED, [2] PINK, [3] GREEN, [4] YELLOW, [5] BLUE");
        do {
            try {
                chosenColor = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error.Invalid input");
            }
        }while(chosenColor<1 || chosenColor>5);

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
     * @param cardID
     * @param expert
     */
    @Override
    public void playExpertType2(int cardID, Expert11 expert) {
        int index;
        int chosenStudent=0;
        do {
            index=1;
            System.out.println("Choose one of the students on this card:");
            for (Color student : expert.getStudentsOnCard()) {
                System.out.println("["+index+"] "+toColor(student, "●"));
                index++;
            }
            try {
                chosenStudent = Integer.parseInt(read())-1;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while(chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size()-1);

        int finalChosenStudent = chosenStudent;
        notifyListener(list -> list.playExpertCard4(cardID, expert.getStudentsOnCard().get(finalChosenStudent)));
    }

    /**
     * method to play expert card 12
     * @param cardID
     * @param expert
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
     * method to play expert card 3
     * @param cardID
     * @param expert
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
            int chosenStudent=0;

            do {
                index=1;
                System.out.println("Choose the student you want to move to your entry hall");
                for (Color student : expert.getStudentsOnCard()) {
                    System.out.println("["+index+"] "+toColor(student, "●"));
                    index++;
                }
                try {
                    chosenStudent = Integer.parseInt(read())-1;
                } catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Error. Invalid input");
                }
            } while(chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size()-1);

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
        notifyListener(list -> list.playExpertCard5( cardID, toEntryHall, toCard));
    }

    /**
     * method to play expert card 10
     * @param cardID
     * @param expert
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
                } catch (ExecutionException| NumberFormatException e) {
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
                    } catch (ExecutionException| NumberFormatException e) {
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
            notifyListener(list -> list.playExpertCard5(cardID ,toDiningRoom, toEntryHall));
    }

    /**
     * method to play expert card 3
     * @param cardID
     * @param expert
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
        } while(chosenID < 0 || chosenID > 12);


        int finalChosenID = chosenID;
        notifyListener(list -> list.playExpertCard2(cardID, finalChosenID));
    }

    /**
     * method to play expert card 1
     * @param cardID
     * @param expert
     */
    @Override
    public void playExpertType5(int cardID, Expert1 expert) {
        int index;
        int chosenStudent=0;

        do {
            index=1;
            System.out.println("Choose one of the students on this card:");
            for (Color student : expert.getStudentsOnCard()) {
                System.out.println("["+index+"] "+toColor(student, "●"));

                index++;
            }
            try {
                chosenStudent = Integer.parseInt(read())-1;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while(chosenStudent < 0 || chosenStudent > expert.getStudentsOnCard().size()-1);

        int chosenID = 0;

        do {
            System.out.println("Choose the island you want to use the effect on: ");
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid parameter");
            }
        } while(chosenID < 0 || chosenID > 12);
        int finalChosenID = chosenID;
        int finalChosenStudent = chosenStudent;
        notifyListener(list -> list.playExpertCard3(cardID, finalChosenID, expert.getStudentsOnCard().get(finalChosenStudent)));
    }

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

    @Override
    public void chooseAction() {
        int chosenAction = 0;
        boolean valid = false;
        System.out.println("Write [1] to play your card, [2] to check the other boards");
        do {
            try {
                chosenAction = Integer.parseInt(read());
                if(chosenAction != 1 && chosenAction != 2)
                    System.out.println("Invalid input");
                else
                    valid = true;
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("Error. Invalid input.");
                valid = false;
            }
        } while(!valid);
        int finalChosenAction = chosenAction;
        notifyListener(list -> list.chooseAction(finalChosenAction));
    }

    public Color availableStudents(List<Color> availableColor) {
        System.out.println("\n\nAvailable students: ");
        Map<Integer, Color> indexColorMap = new HashMap<>();
        int choice = 0;
        int index = 1;

        for(Color currentColor : availableColor) {
            if(currentColor.equals(Color.RED)) {
                System.out.println("[" +index+ "] RED ");
                index++;
            }
            if(currentColor.equals(Color.RED)) {
                System.out.println("[" +index+ "] PINK ");
                index++;
            }
            if(currentColor.equals(Color.RED)) {
                System.out.println("[" +index+ "] GREEN ");
                index++;
            }
            if(currentColor.equals(Color.RED)) {
                System.out.println("[" +index+ "] YELLOW ");
                index++;
            }
            if(currentColor.equals(Color.RED)) {
                System.out.println("[" +index+ "] BLUE ");
                index++;
            }

            indexColorMap.put(index, currentColor);
        }

        do {
            try {
                System.out.println("Choose: ");
                choice = Integer.parseInt(read());
            } catch (ExecutionException | NumberFormatException e) {
                System.out.println("This student is not available");
            }
        } while (!indexColorMap.containsKey(choice));
        return indexColorMap.get(choice);
    }

    @Override
    public void chooseExpertCard() {
        int chosenExpert = 0;
        boolean valid = true;
        do{
            System.out.println("Write the ID of the Expert card you want to play: [0] to return");
            try {
                chosenExpert = Integer.parseInt(read())-1;
            } catch (ExecutionException | NumberFormatException e) {
                valid = false;
            }
            if(chosenExpert<-1 || chosenExpert>2)
                valid = false;
            if(!valid)
                System.out.println("Error. Invalid parameter");
        } while (chosenExpert<-1 || chosenExpert>2);

        if(chosenExpert != -1) {
            int finalChosenExpert = chosenExpert;
            notifyListener(list -> list.applyExpertEffect(finalChosenExpert));
        }
        else
            notifyListener(list -> list.askAction(true));

    }

    @Override
    public void expertModeControl(boolean setExpertMode) {

    }

    public void playExpertChoice() {
            int choice = 0;

            do {
                System.out.println("Write [1] to move Mother Nature, [2] to play an Expert");
                try {
                    choice = Integer.parseInt(read());
                }
                catch (ExecutionException | NumberFormatException e) {
                    System.out.println("Errror. Invalid input");
                }
            }
            while (!(choice == 1 || choice == 2));

            final int finalChoice = choice;

            if(finalChoice == 1) //49 is ascii number for 1
                moveMotherNature();
            else notifyListener(list -> list.actionPhaseChoice(MessageType.EXPERT_CARD_REQ));
    }

    private void printStudentsInsideExpertCard(Expert1 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription()+"\nStudents on this card: ");
        for(Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student,"● "));
        }
        System.out.println();
    }

    private void printStudentsInsideExpertCard(Expert7 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription()+"\nStudents on this card: ");
        for(Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student,"● "));
        }
        System.out.println();
    }

    private void printStudentsInsideExpertCard(Expert11 expert, int index) {
        System.out.print("Expert [" + index + "]\nExpert card cost: " + expert.getCost() + "\nExpert card effect: " + expert.getExpDescription()+"\nStudents on this card: ");
        for(Color student : expert.getStudentsOnCard()) {
            System.out.print(toColor(student,"● "));
        }
        System.out.println();
    }

}
