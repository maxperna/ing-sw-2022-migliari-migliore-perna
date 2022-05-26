package it.polimi.ingsw.view;

import com.sun.javafx.collections.MappingChange;
import it.polimi.ingsw.gameField.Node;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.ExpertID;
import it.polimi.ingsw.network.messages.Message;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Cli extends ViewSubject implements View {
    ArrayList<Listener> list = new ArrayList();
    Scanner scan;

    public Cli() {
        this.scan = new Scanner(System.in);
    }

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


    public void connectionRequest() {
        HashMap<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        String address;
        String port;
        System.out.println("Choose the server address: [" + defaultAddress + "]");
        address = scan.next();
        if (address.equals(""))
            serverInfo.put("address", defaultAddress);
         else
            serverInfo.put("address", address);

        System.out.println("Choose the server port: ["+defaultPort+"]");
        port = scan.next();
        serverInfo.put("port", port);


        System.out.println("\n\nWaiting for game creation...");

        notifyListener(list -> list.connectionRequest(serverInfo));

    }


    public void askPlayerNickname() {
        String nickname = "";

        do {
            System.out.print("Insert your nickname here: ");
            nickname = this.scan.next();
            if (nickname.equals("")) {
                System.out.println("Invalid input");
            }
        } while(nickname.equals(""));

        String finalNickname = nickname;
        this.notifyListener((list) -> {
            list.sendNickname(finalNickname);
        });
    }

    public void printText(String text) {
    }

    public void askGameParam() {
        boolean expert = false;

        int numOfPlayers;
        do {
            System.out.print("Choose the number of players [2, 3, 4]: ");
            numOfPlayers = Integer.parseInt(scan.next());
            if (numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4) {
                System.out.println("Invalid parameter");
            }
        } while(numOfPlayers != 2 && numOfPlayers != 3 && numOfPlayers != 4);

        String expertMode;
        do {
            System.out.print("Do you want to play in expert mode? [Y/N] ");
            expertMode = this.scan.next().toUpperCase(Locale.ROOT);
            if (!expertMode.contains("Y") && !expertMode.contains("N")) {
                System.out.println("Invalid parameter");
            }
        } while(!expertMode.contains("Y") && !expertMode.contains("N"));

        if (expertMode.contains("Y") && !expertMode.contains("N")) {
            expert = true;
        }

        int finalNumOfPlayers = numOfPlayers;
        boolean finalExpert = expert;
        this.notifyListener((list) -> {
            list.sendGameParam(finalNumOfPlayers, finalExpert);
        });
    }

    public void showRemainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
        System.out.println("Remaining tower: "+remainingTowers);
        System.out.println("Remaining deck: "+remainingDecks);

        System.out.println("\n\n");

        System.out.println("Select tower:");
        int tower = scan.nextInt();
        System.out.println("Select deck:");
        int deck = scan.nextInt();

        this.notifyListener((list)->{
                list.chooseTowerColorAndDeck(remainingTowers.get(tower),remainingDecks.get(deck));
        });

    }

    public void showInitPlayer(int numberOfTowers, ArrayList<Color> entranceHall) {
    }

    public void showGameField(Map<Integer, Node> gameFieldMap) {
    }

    public void showClouds(ArrayList<CloudTile> newClouds) {
    }

    public void showCurrentPlayer(String currentPlayer) {
    }

    public void showExpertCards(ArrayList<ExpertID> expertIDList) {
    }

    public void updateTeachers(Map<Color, Boolean> teacherList) {
    }

    public void updateNode(Node updatedNode) {
    }

    public void showGenericMessage(String genericMessage) {
        System.out.println(genericMessage);
    }

    public void newCoin(String player, int numOfCoin) {
    }

    public void showWinner(String winner) {
    }

    public void showError(String errorMessage) {
    }

    public void showExpertID(ArrayList<ExpertID> expertID) {
    }

    public void getServerInfo() {
    }


    public void sendSelectedID(ArrayList<Integer> ID) {
        int chosenID;
        do {
            System.out.println("Choose one from the available objects: " + ID.toString());
            chosenID = Integer.parseInt(this.scan.next());
            if (!ID.contains(chosenID)) {
                System.out.println("Invalid input");
            }
        } while(!ID.contains(chosenID));

        int finalChosenID = chosenID;
        this.notifyListener((list) -> {
            list.sendSelectedID(finalChosenID);
        });
    }

    /*public void selectStudent(ArrayList<Color> students) {
        Color finalColor = null;

        String color;
        do {
            System.out.println("Choose the color of the student you want to move: " + students.toString());
            color = this.scan.next().toUpperCase(Locale.ROOT);
            if (!color.contains("GREEN") && !color.contains("PINK") && !color.contains("RED") && !color.contains("YELLOW") && !color.contains("BLUE")) {
            }

            System.out.println("Invalid parameter");
        } while(!color.contains("GREEN") && !color.contains("PINK") && !color.contains("RED") && !color.contains("YELLOW") && !color.contains("BLUE"));

        switch (color.toUpperCase(Locale.ROOT)) {
            case "RED":
                finalColor = Color.RED;
                break;
            case "YELLLOW":
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
            notifyListener(list -> list.moveStudentToIsland();
        }

    }*/

    public void catchAction(Message receivedMessage) {
    }

    public void chooseTowerColorAndDeckType(ArrayList<TowerColor> availableColors, ArrayList<DeckType> availableDecks) {
        TowerColor finalColor = null;
        Boolean valid = false;
        String deckChosen;
        DeckType deck = null;

        String towerColor;
        do {
            System.out.println("Choose your tower color: " + availableColors.toString());
            towerColor = this.scan.next().toUpperCase(Locale.ROOT);
            if (availableColors.size() == 3) {
                if (!towerColor.contains("WHITE") && !towerColor.contains("BLACK") && towerColor.contains("GREY")) {
                }

                valid = true;
            } else if (availableColors.size() == 2) {
                if (!towerColor.contains("WHITE") && towerColor.contains("BLACK")) {
                }

                valid = true;
            } else {
                System.out.println("Invalid parameter");
            }
        } while(!valid);

        switch (towerColor) {
            case "BLACK":
                finalColor = TowerColor.BLACK;
                break;
            case "WHITE":
                finalColor = TowerColor.WHITE;
                break;
            case "GRAY":
                finalColor = TowerColor.GRAY;
                break;
        }

        TowerColor finalColor1 = finalColor;

        do {
            System.out.print("Choose your deck of cards: " + availableDecks.toString());
            deckChosen = this.scan.next().toUpperCase(Locale.ROOT);
            if (!availableDecks.toString().contains(deckChosen)) {
                System.out.println("Invalid input");
            }
        } while(!availableDecks.toString().contains(deckChosen));

        switch (deckChosen) {
            case "DRUID":
                deck = DeckType.DRUID;
                break;
            case "SAGE":
                deck = DeckType.SAGE;
                break;
            case "WITCH":
                deck = DeckType.WITCH;
                break;
            case "KING":
                deck = DeckType.KING;
                break;
        }

        DeckType finalDeck = deck;

        notifyListener(list -> list.chooseTowerColorAndDeck(finalColor1, finalDeck));
    }


    public String chooseDestination() {
        String destination;
        do {
            System.out.println("Choose the destination of the student: [BOARD], [ISLAND]");
            destination = this.scan.next().toUpperCase(Locale.ROOT);
            if (!destination.toUpperCase().equals("BOARD") && !destination.toUpperCase().equals("ISLAND")) {
                System.out.println("Invalid input");
            }
        } while(!destination.toUpperCase().equals("BOARD") && !destination.toUpperCase().equals("ISLAND"));

        return destination;
    }

    public void getPlayerInfo(ArrayList<String> players) {
        String player;
        do {
            System.out.println("Choose the player you want to get info on: " + players.toString());
            player = this.scan.next();
            if (player.equals("") || !players.toString().contains(player)) {
                System.out.println("Invalid input");
            }
        } while(player.equals(""));

        String finalPlayer = player;
        this.notifyListener((list) -> {
            list.getPlayerInfo(finalPlayer);
        });
    }

    public void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {
    }

    public void disconnect() {
    }
}
