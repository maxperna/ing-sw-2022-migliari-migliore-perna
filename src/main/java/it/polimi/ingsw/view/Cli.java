package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.FirstLoginMessage;
import it.polimi.ingsw.network.messages.LogInMessage;

import java.util.ArrayList;
import java.util.Scanner;

public class Cli implements View{
    Listener listener = new Listener();
    Scanner scan = new Scanner(System.in);
    ArrayList<String> players;
    ArrayList<DeckType> deckAvailable;
    ArrayList<TowerColor> towerAvailable;


    public Cli () {
        players = new ArrayList<>();
        deckAvailable = new ArrayList<>();
        towerAvailable = new ArrayList<>();
        towerAvailable.add(TowerColor.BLACK);
        towerAvailable.add(TowerColor.GRAY);
        towerAvailable.add(TowerColor.WHITE);
        deckAvailable.add(DeckType.SAGE);
        deckAvailable.add(DeckType.KING);
        deckAvailable.add(DeckType.WITCH);
        deckAvailable.add(DeckType.DRUID);
    }

    @Override
    public void start() {
        System.out.println(""+
          "▄▄                                              ██"+
          "▀███▀▀▀███                                      ██    ██"+
           "██   █  ▀███▄███▀██▀   ▀██▀▄█▀██▄ ▀████████▄ ██████▀███  ▄██▀███"+
           "██████    ██▀ ▀▀  ██   ▄█ ██   ██   ██    ██   ██    ██  ██   ▀▀"+
           "██   █  ▄ ██       ██ ▄█   ▄█████   ██    ██   ██    ██  ▀█████▄"+
           "██     ▄█ ██        ███   ██   ██   ██    ██   ██    ██  █▄   ██"+
           "▄██████████████▄    ▄█    ▀████▀██▄████  ████▄ ▀████████▄██████▀"+
           "                   ▄█"+
           "                 ██▀"

    );
        System.out.println("Welcome to Eryantis");
    }

    /*public void getServerInfo() throws ExecutionException {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;
        Scanner scan = new Scanner(System.in);

        System.out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            System.out.print("Enter the server address [" + defaultAddress + "]: ");

            String address = scan.next();

            if (address.equals("")) {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            } else if (ClientController.isValidIpAddress(address)) {
                serverInfo.put("address", address);
                validInput = true;
            } else {
                System.out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);

        do {
            System.out.print("Enter the server port [" + defaultPort + "]: ");
            String port = System.in.toString();

            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true;
                } else {
                    System.out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);

        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
    }*/


    /**
     * method used to ask game mode, expert mode ,nickname, tower color, deck to the first player, only the latter 3 to the other player(s)
     */
    @Override
    public void askPlayerInfo(){
        String nickname = "";
        TowerColor towerColor = null;
        DeckType deck = null;
        String deckChosen = "";
        String colorChosen = "";
        int numOfPlayers = 0;
        String gameMode = "";
        Boolean expert = false;

        do{
            System.out.print("Insert your nickname here: ");
            nickname = scan.next();
            if(nickname.equals(""))
                System.out.println("Invalid input");
            if(players.contains(nickname))
                System.out.println("This nickname is already taken");
        }while(!nickname.equals(""));

        players.add(nickname);                                                                                          //adding the nickname to a list to check for uniqueness of following nicknames

        if(players.size() == 1) {                                                                                       //if it's the first player, ask for game mode and expert mode
            String expertMode;
            do {
                System.out.print("Choose the number of players [2, 3, 4]: ");
                numOfPlayers = Integer.parseInt(scan.next());
                if(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4))
                    System.out.println("Invalid parameter");
            }while(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4));

            switch(numOfPlayers) {                                                                                      //switch used to convert the value provided by the player to a string
                case 2: gameMode = "TwoPlayers";
                    break;
                case 3: gameMode = "ThreePlayers";
                    break;
                case 4: gameMode = "FourPlayers";
            }

            do {
                System.out.print("Do you want to play in expert mode? [Y/N] ");
                expertMode = scan.next();
                if(!(expertMode.contains("Y") || expertMode.contains("N")))
                    System.out.println("Invalid parameter");
            }while(!(expertMode.contains("Y") || expertMode.contains("N")));

            if(expertMode.contains("Y"))
                expert = true;
        }

        if(numOfPlayers == 3) {                                                                                         //case used to choose the color of towers between 3 different values
            do{
                System.out.print("Choose the color of your towers: " + towerAvailable.toString());
                colorChosen = scan.next();
                if(!(towerAvailable.toString().contains(colorChosen)))                                                  //check that the value received from the player is between the available ones
                    System.out.println("Invalid input");
            }while(!(towerAvailable.toString().contains(colorChosen)));

            switch(colorChosen) {                                                                                       //switch used to get a TowerColor parameter from the input
                case "BLACK": towerColor = TowerColor.BLACK;
                    break;
                case "WHITE": towerColor = TowerColor.WHITE;
                    break;
                case "GRAY": towerColor = TowerColor.GRAY;
            }

            towerAvailable.remove(colorChosen);                                                                         //remove the chosen color from the arrayList of available colors
        }

        else if(numOfPlayers == 2 || numOfPlayers == 4) {
            towerAvailable.remove(TowerColor.GRAY);                                                                     //remove gray towers if it's a 2-4 player game
            do{
                System.out.print("Choose the color of your towers: " + towerAvailable.toString());
                colorChosen = scan.next();
                if(!(towerAvailable.toString().contains(colorChosen)))
                    System.out.println("Invalid input");
            }while(!(towerAvailable.toString().contains(colorChosen)));

            switch(colorChosen) {
                case "BLACK": towerColor = TowerColor.BLACK;
                    break;
                case "WHITE": towerColor = TowerColor.WHITE;
                    break;
                case "GRAY": towerColor = TowerColor.GRAY;
            }

            towerAvailable.remove(towerColor);                                                                          //removing the chosen tower color
        }

        do{
            System.out.print("Choose your deck of cards: " + deckAvailable);
            deckChosen = scan.next();
            if(!(deckAvailable.toString().contains(deckChosen)))
                System.out.println("Invalid input");
        }while(!(deckAvailable.toString().contains(deckChosen)));

        switch(deckChosen) {
            case "DRUID": deck = DeckType.DRUID;
                break;
            case "SAGE": deck = DeckType.SAGE;
                break;
            case "WITCH": deck = DeckType.WITCH;
                break;
            case "KING": deck = DeckType.KING;
                break;
        }

        deckAvailable.remove(deck);

        if(players.size() == 1)                                                                                         //if it's the first player, send a FirstLoginMessage containing all the info required to start the game
            listener.notifyListener(new FirstLoginMessage(nickname, gameMode, expert, towerColor, deck));
        else
            listener.notifyListener(new LogInMessage(nickname, towerColor, deck));
    }


    @Override
    public void askMoveToBoard() {
        String studentToBeMoved;
        System.out.print("Choose the student you want to move to your board: ");
        studentToBeMoved = scan.next();
        //call to moveStudentToBoard method
    }

    @Override
    public void askMoveToIsland() {
        String studentToBeMoved;
        String islandID;
        System.out.print("Choose the student you want to move: ");
        studentToBeMoved = scan.next();
        System.out.print("Choose the island you want to move your student in: ");
        islandID = scan.next();
    }

    @Override
    public void useAssistantEffect() {
        System.out.print("Insert the ID of the Assistant you want to use [1, 2, 3]: ");
    }

    @Override
    public void askCardToPlay() {
        System.out.print("Insert the ID of the card you want to play: ");
    }

    @Override
    public void askIslandInfo() {
        System.out.print("Insert the ID of the island you want ot get the info from: ");
    }

    @Override
    public void showLoginResult() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showGenericMessage() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showDisconnectionMessage() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showErrorAndExit() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showLobby() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showMatchInfo() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void askFirstPlayer() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void showWinMessage() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void chooseCloud() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void chooseMotherNatureMoves() {
        System.out.print("Choose the student you want to move to your board: ");
    }

    @Override
    public void chooseMotherNaturePosition() {
        System.out.print("Choose the student you want to move to your board: ");
    }
}
