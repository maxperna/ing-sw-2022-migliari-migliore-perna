package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.gameField.Node;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.observer.Listener;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.View;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewSubject implements View {

    List<Listener> list = new ArrayList();
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

        askPlayerNickname();

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

        System.out.println("Choose the server port: ["+defaultPort+"]");
        /*try {
            port = read();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        if(port == null)
            serverInfo.put("port", defaultPort);
        else
            serverInfo.put("port", port);


        System.out.println("\n\nWaiting for game creation...");

        notifyListener(list -> list.connectionRequest(serverInfo));

    }

    @Override
    public void showLastUsedCard(AssistantCard card, String playerName) {
        StringBuilder string = new StringBuilder();
        System.out.println("This is " +playerName+"'s last used card");
        string.append("_".repeat(10));

        System.out.println(ANSI_RED+string);
        string.delete(0, string.capacity());

        if(card.getActionNumber() < 10)
            string.append("| "+card.getActionNumber()+"    "+(int)Math.ceil((float)card.getActionNumber()/2)+" |     ");
        else
            string.append("| "+card.getActionNumber()+"   "+(int)Math.ceil((float)card.getActionNumber()/2)+" |     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        System.out.println("|        |");
        System.out.println("|        |");
        if(card.getActionNumber() < 10)
            string.append("| "+card.getActionNumber()+"    "+(int)Math.ceil((float)card.getActionNumber()/2)+" |     ");
        else
            string.append("| "+card.getActionNumber()+"   "+(int)Math.ceil((float)card.getActionNumber()/2)+" |     ");
        System.out.println(string);
        System.out.println("----------");
        string.delete(0, string.capacity());
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
        } while(nickname.equals(""));

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
        boolean expert = false;

        int numOfPlayers = 0;
        do {
            System.out.print("Choose the number of players [2, 3, 4]: ");
            try {
                numOfPlayers = Integer.parseInt(read());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4) {
                System.out.println("Invalid parameter");
            }
        } while(numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4);

        String expertMode = null;
        do {
            System.out.print("Do you want to play in expert mode? [Y/N] ");
            try {
                expertMode = read().toUpperCase(Locale.ROOT);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!expertMode.equals("Y") && !expertMode.equals("N")) {
                System.out.println("Invalid parameter");
            }
        } while(!expertMode.equals("Y") && !expertMode.equals("N"));

        if (expertMode.equals("Y")) {
            expert = true;
        }

        int finalNumOfPlayers = numOfPlayers;
        boolean finalExpert = expert;
        this.notifyListener((list) -> {
            list.sendGameParam(finalNumOfPlayers, finalExpert);
        });
    }

    /**
     * method used to let player choose its deck and tower color based on a list of this parameters available
     * @param remainingTowers list of remaining available towerColor
     * @param remainingDecks list of remaining available deckType
     */
    @Override
    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        int tower = 0;
        int deck = 0;
        do {
            System.out.println();
            System.out.println("Remaining tower: "+remainingTowers);
            System.out.println("Select tower by putting its position (1 to "+remainingTowers.size()+"): ");

            try {
                tower = Integer.parseInt(read())-1;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(tower > remainingTowers.size()  || tower < 0)
                System.out.println("Error. Invalid input");

        } while(tower > remainingTowers.size() || tower < 0);

        do {
            System.out.println();
            System.out.println("Remaining deck: "+remainingDecks);
            System.out.println("Select deck by putting its position (1 to "+remainingDecks.size()+"): ");

            try {
                deck = Integer.parseInt(read())-1;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(deck > remainingDecks.size() || deck < 0)
                System.out.println("Error. Invalid input");

        } while (deck > remainingDecks.size() || deck < 0);


        int finalTower = tower;
        int finalDeck = deck;
        this.notifyListener((list)->{
                list.chooseTowerColorAndDeck(remainingTowers.get(finalTower),remainingDecks.get(finalDeck));
        });
        clearCli();

    }

    @Override
    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {

    }

    /**
     * method used to print the island list and then to call the chooseBoard() method
     * @param gameFieldMap map with the gameField
     */
    @Override
    public void showGameField(Map<Integer, Node> gameFieldMap) {
        System.out.println();
        if(tutorial) {
            System.out.println("This is the game field: it shows the students set on every island, the dot shows Mother Nature position,");
            System.out.println("the number indicates the towers set on that island (2 or more if it is a super island); the last symbol shows that the island is blocked (expert only)");
        }
        System.out.println();
        for(int i=1; i<=gameFieldMap.size(); i++) {
            if(i<10)
                System.out.print("Island " +(i)+": ");
            else
                System.out.print("Island " +(i)+":");
            for(int j=0; j<gameFieldMap.get(i).getStudents().size(); j++) {
                switch (gameFieldMap.get(i).getStudents().get(j)) {
                    case RED: System.out.print(ANSI_RED + "██ " + ANSI_RESET);
                        break;
                    case BLUE: System.out.print(ANSI_BLUE + "██ " + ANSI_RESET);
                        break;
                    case GREEN: System.out.print(ANSI_GREEN + "██ " + ANSI_RESET);
                        break;
                    case YELLOW: System.out.print(ANSI_YELLOW + "██ " + ANSI_RESET);
                        break;
                    case PINK: System.out.print(ANSI_PINK + "██ " + ANSI_RESET);
                        break;
                }
            }
            if (gameFieldMap.get(i).checkMotherNature() && gameFieldMap.get(i).getStudents().size() == 0)
                System.out.print("    ● ");
            else if (gameFieldMap.get(i).checkMotherNature())
                System.out.print("  ●");
            else if (gameFieldMap.get(i).getStudents().size() == 0)
                System.out.print("      ");
            else
                System.out.print("   ");

            if (gameFieldMap.get(i).getNumberOfTowers() != 0)
                System.out.print(" " +gameFieldMap.get(i).getNumberOfTowers()+ " " +gameFieldMap.get(i).getTowerColor().toString());
            else
                System.out.print("   ");

            if (gameFieldMap.get(i).isStopped())
                System.out.print("  ⦻");

            System.out.println();
        }
        System.out.println();
    }

    /**
     * method used to print the students inside all the cloud tiles
     * @param newClouds arrayList that contains all the clouds in the game
     */
    @Override
    public void showClouds(ArrayList<CloudTile> newClouds) {
        for (CloudTile cloud : newClouds) {
            System.out.print("Cloud " + cloud.getTileID() + " contains the following students: ");
            for (int j = 0; j < cloud.getStudents().size(); j++) {
                System.out.print(toColor(cloud.getStudents().get(j), "██ "));
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void showCurrentPlayer(String currentPlayer, GameState currentState) {

    }

    public void showExpertCards(ArrayList<ExpertID> expertIDList) {
    }

    public void updateTeachers(Map<Color, Boolean> teacherList) {
    }

    public void updateNode(Node updatedNode) {
        System.out.println("A change happened on an island");
    }

    public void showGenericMessage(String genericMessage) {
        System.out.println(genericMessage);
    }

    public void newCoin(String player, int numOfCoin) {
    }

    @Override
    public void showPlayedAssistantCard(Map<String, AssistantCard> lastCardMap) {

    }

    /**
     * method used to print the winning message
     * @param winner nickname of the winner
     */
    public void showWinner(String winner) {
        System.out.println("Congratulations! " +winner+ " is the winner!");
    }

    public void showError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public void showExpertID(ArrayList<ExpertID> expertID) {
    }

    /**
     * method used to print the description of each expert card
     * @param expertCard
     */
    @Override
    public void showExpertCard(ArrayList<ExpertCard> expertCard) {
        for(ExpertCard expert : expertCard)
            System.out.println("Expert card cost: "+expert.getCost()+"/nExpert card effect: " +expert.getExpDescription());
    }

    /**
     * method used to get from input an ID from a list of given ones
     * @param ID
     */
    public void sendSelectedID(ArrayList<Integer> ID) {
        int chosenID = 0;
        do {
            System.out.println("Choose one from the available objects: " + ID.toString());
            try {
                chosenID = Integer.parseInt(read());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!ID.contains(chosenID)) {
                System.out.println("Invalid input");
            }
        } while(!ID.contains(chosenID));

        int finalChosenID = chosenID;
        this.notifyListener((list) -> {
            list.sendSelectedID(finalChosenID);
        });
    }

    /**
     * method used to get a student color from all the available ones and to get the destination
     * @param students is the arraylist containing all the available students
     * @param islands is an int that contains the id of the last island, basically indicating the number of islands remaining
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
        } while(!color.contains("GREEN") && !color.contains("PINK") && !color.contains("RED") && !color.contains("YELLOW") && !color.contains("BLUE"));

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

    public void catchAction(Message receivedMessage) {
    }

    /**
     * method called by selectStudent() to get the destination
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
        } while(!destination.toUpperCase().equals("BOARD") && !destination.toUpperCase().equals("ISLAND"));

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
        } while(player.equals(""));

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
        if(tutorial) {
            System.out.println("This is your card deck. On each card, the red number is the card's action number, the white number is the maximum number of moves that Mother Nature can perform");
            tutorial = false;
        }
        StringBuilder string = new StringBuilder();
        int index;
        for (index=0; index<deck.size(); index++)
            string.append("__________     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index=0; index<deck.size(); index++){
            if(deck.get(index).getActionNumber() < 10)
                string.append("| "+ANSI_RED+deck.get(index).getActionNumber()+ANSI_RESET+"    "+(int)Math.ceil((float)deck.get(index).getActionNumber()/2)+" |     ");
            else
                string.append("| "+ANSI_RED+deck.get(index).getActionNumber()+ANSI_RESET+"   "+(int)Math.ceil((float)deck.get(index).getActionNumber()/2)+" |     ");
        }

        System.out.println(string);
        string.delete(0, string.capacity());
        for (index=0; index<deck.size(); index++)
            string.append("|        |     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index=0; index<deck.size(); index++)
            string.append("|        |     ");
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index=0; index<deck.size(); index++){
            if(deck.get(index).getActionNumber() < 10)
                string.append("| "+ANSI_RED+deck.get(index).getActionNumber()+ANSI_RESET+"    "+(int)Math.ceil((float)deck.get(index).getActionNumber()/2)+" |     ");
            else
                string.append("| "+ANSI_RED+deck.get(index).getActionNumber()+ANSI_RESET+"   "+(int)Math.ceil((float)deck.get(index).getActionNumber()/2)+" |     ");
        }
        System.out.println(string);
        string.delete(0, string.capacity());
        for (index=0; index<deck.size(); index++)
            string.append("----------     ");
        System.out.println(string);
        string.delete(0, string.capacity());

        int choice = 0;
        try {
            choice = Integer.parseInt(read());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int finalChoice = choice;
        this.notifyListener((list)->{
            list.playAssistantCard(finalChoice);
        });
    }

    public void clearCli() {
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

    }

    @Override
    public void printBoard(Board board) {

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
                    if (studentIndex < hallDimension-1) {
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
     * @param color is the color of the string that will be printed
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
     * @param color is the color of the row we are considering
     * @param numOfColor is the number of students of that given color that are set on the dining room
     * @param teacher is a boolean that indicates if the board passed to the showBoard() method contains the teacher of the given color
     */
    private void printStudentInsideDiningRoom(Color color, int numOfColor, Boolean teacher) {

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

    /**
     * method used to get from the user the board he wants to check
     * @param boardMap is a map of all the available boards in the game
     */
    @Override
    public void showBoard(Map<String, Board> boardMap) {
        int index = 0;
        Map<Integer, String> integerToString = new HashMap<>();
        int chosenPlayer = 0;
        do {
            try {

                index = 0;
                System.out.println("Choose which board you want to check: ");
                for(String s : boardMap.keySet()) {
                    System.out.println("["+index+"] for "+ s);
                    integerToString.put(index, s);
                    index++;
                }
                System.out.println("["+index+"] for all boards");

                chosenPlayer = Integer.parseInt(read());

                if((chosenPlayer < 0 || chosenPlayer > boardMap.size()+1))
                    System.out.println("Invalid parameter");

            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } while((chosenPlayer < 0 || chosenPlayer > boardMap.size()+1));

        if(chosenPlayer == index) {
            for(Board b : boardMap.values())
                printBoard(b);
        }
        else
            printBoard(boardMap.get(integerToString.get(chosenPlayer)));

    }

    private String shouldPrintStudent(int studentID, Board board, String string) {
        if(studentID < board.getEntryRoom().size()) {
            return toColor(board.getEntryRoom().get(studentID), string);
        }
        else return "-- ";
    }

    @Override
    public void ActionPhaseTurn(){

        System.out.println("\n1 to dinner, 2 to island");

            int choice = 0;

            try {
                choice = Integer.parseInt(read());

                if (choice == 1 ) {
                    this.notifyListener((list) -> {
                        list.moveStudentToDinner(colorSelector());
                    });

                } else if (choice == 2 ) {
                    int islandID;
                    System.out.println("number: ");
                    islandID = Integer.parseInt(read());

                    this.notifyListener((list) -> {
                        list.moveStudentToIsland(colorSelector(), islandID);
                    });

                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void moveMotherNature() {

        System.out.println("MoveMotherNature: ");

            try {
                int num = Integer.parseInt(read());
                this.notifyListener((list) -> {
                    list.moveMotherNature(num);
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

    }

    @TestOnly
    Color colorSelector() {
        System.out.println("\n\n1 Red, 2 Pink, 3Blue, 4Green, 5Yellow");
        int choice = 0;
        try {
            choice = Integer.parseInt(read());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        switch (choice){
            case 1:
                return Color.RED;
            case 2:
                return Color.PINK;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.YELLOW;
            default:
                return Color.BLUE;
        }
    }

    public void chooseAction() {
        int chosenAction = 0;
        System.out.println("Write [1] to play your card, [2] to check the other boards, (3 to play Expert)");
        do {
            try {
                chosenAction = Integer.parseInt(read());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(chosenAction != 1 && chosenAction != 2)
                System.out.println("Invalid input");
        } while(chosenAction != 1 && chosenAction != 2);
        int finalChosenAction = chosenAction;
        notifyListener(list -> list.chooseAction(finalChosenAction));
    }

    public void chooseCloudTile(int numberOfPlayers) {
        int chosenCloud = 0;
        do {
            try {

                System.out.println("Choose cloud: ");
                for(int i = 0; i<numberOfPlayers; i++ ) {
                    System.out.println("["+i+"]");
                }
                chosenCloud = Integer.parseInt(read());

                if((chosenCloud < 0 || chosenCloud > numberOfPlayers))
                    System.out.println("Invalid parameter");

            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } while((chosenCloud < 0 || chosenCloud > numberOfPlayers));


    }

}
