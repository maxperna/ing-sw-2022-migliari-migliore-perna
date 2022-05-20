package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.Message;

import java.util.*;

public class Cli extends ViewSubject implements View {
    ArrayList<Listener> list = new ArrayList<>();
    Scanner scan = new Scanner(System.in);
    ArrayList<String> players;
    ArrayList<DeckType> decksAvailable;
    ArrayList<TowerColor> towerAvailable;
    String clientPlayer;


    public Cli () {
        players = new ArrayList<>();
        decksAvailable = new ArrayList<>();
        towerAvailable = new ArrayList<>();
        towerAvailable.add(TowerColor.BLACK);
        towerAvailable.add(TowerColor.GRAY);
        towerAvailable.add(TowerColor.WHITE);
        decksAvailable.add(DeckType.SAGE);
        decksAvailable.add(DeckType.KING);
        decksAvailable.add(DeckType.WITCH);
        decksAvailable.add(DeckType.DRUID);
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

        //
    }


    /**
     * method used to ask nickname
     */
    @Override
    public void askPlayerNickname() {
        String nickname = "";

        do {
            System.out.print("Insert your nickname here: ");
            nickname = scan.next();
            if (nickname.equals(""))
                System.out.println("Invalid input");

        } while (!nickname.equals(""));
        String finalNickname = nickname;
        notifyListener(list -> list.sendNickname(finalNickname));
    }

    @Override
    public void printText(String text) {

    }

    @Override
    public void sendGameParam() {
        int numOfPlayers;
        String expertMode;
        boolean expert = false;
        do {
            System.out.print("Choose the number of players [2, 3, 4]: ");
            numOfPlayers = Integer.parseInt(scan.next());
            if(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4))
                System.out.println("Invalid parameter");
        }while(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4));

        do {
            System.out.print("Do you want to play in expert mode? [Y/N] ");
            expertMode = scan.next().toUpperCase(Locale.ROOT);
            if(!(expertMode.contains("Y") || expertMode.contains("N")))
                System.out.println("Invalid parameter");
        }while(!(expertMode.contains("Y") || expertMode.contains("N")));

        if(expertMode.contains("Y") && !expertMode.contains("N"))
            expert = true;

        int finalNumOfPlayers = numOfPlayers;
        boolean finalExpert = expert;
        notifyListener(list -> list.sendGameParam(finalNumOfPlayers, finalExpert));
    }

    @Override
    public void getServerInfo() {

    }


    /*if(numOfPlayers == 3) {                                                                                         //case used to choose the color of towers between 3 different values
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

    listener.notifyListener(new CreatePlayerMessage(nickname, towerColor, deck));
    if(players.size() == 1)                                                                                         //if it's the first player, send a FirstLoginMessage containing all the info required to start the game
        listener.notifyListener(new GameParamMessage(nickname, numOfPlayers, expert));
    listener.notifyListener(new EndLogInMessage());
}


@Override
public void askMoveTo() {
    String studentToBeMoved;
    String destination;
    String islandID;
    int lastIslandID;
    ArrayList<Color> studentsAvailable;
    //method to get info player students
    do {
        System.out.print("Choose the student by writing its color: " +studentsAvailable.toString() );
        studentToBeMoved = scan.next();
        if(!studentsAvailable.contains(studentToBeMoved))
            System.out.println("Invalid input");
    }while (!studentsAvailable.contains(studentToBeMoved));

    do {
        System.out.println("Where do you want to move your student? [BOARD], [ISLAND]");
        destination = scan.next();
        if(!(destination.contains("BOARD") || destination.contains("ISLAND")))
            System.out.println("Invalid input");
    } while(!(destination.contains("BOARD") || destination.contains("ISLAND")));

    if(destination.contains("BOARD"))
        //listener.notify;
    if(destination.contains("ISLAND")) {
        do {
            System.out.print("Available islands: ");
            for(int i=1; i<=lastIslandID; i++)
                System.out.print("["+i+"]"+" ");
            System.out.print("Choose the island of destination: ");
            islandID = scan.next();
            if(Integer.parseInt(islandID) < 1 || Integer.parseInt(islandID) > lastIslandID)
                System.out.println("Invalid input");
        }while (Integer.parseInt(islandID) < 1 || Integer.parseInt(islandID) > lastIslandID);
        //listener.notify;
    }

}

@Override
public void useExpertEffect() {
    System.out.print("Insert the ID of the Assistant you want to use [1, 2, 3]: ");
}

@Override
public void askCardToPlay(Card availableCards) {
    int chosenCard;
    ArrayList<Integer> availableCards = new ArrayList<>();
    for(int i=1; i<11; i++)
        availableCards.add(i);
    do {
        System.out.print("These are the available cards ID and the maximum number of moves allowed: ");
        for(int n : availableCards)
            System.out.print("["+availableCards.get(n)+"]"+"[" +(int)Math.ceil((double)(availableCards.get(n))/2)+ ", ");
        System.out.print("Insert the ID of the card you want to play: ");
        chosenCard = Integer.parseInt(scan.next());
        if(!availableCards.contains(chosenCard))
            System.out.println("Invalid input");
    } while(!availableCards.contains(chosenCard));
    availableCards.remove(chosenCard);
    //listener;
}

@Override
public void askIslandInfo() {
    int lastIslandID;
    int ID;
    Node island;
    do {
        System.out.println("Choose the island of your interest: ");
        //method to get last island ID
        ID = Integer.parseInt(scan.next());
        if (ID < 0 || ID > lastIslandID)
            System.out.println("Invalid input");
    } while(ID < 0 || ID > lastIslandID);
    //listener.notifyListener()
    //method to get islandInfo
    System.out.println("The chosen island has the following students: ");
    for(Color color : Color.values()) {
        if(island.getColorInfluence(color) == 1)
            System.out.print(island.getColorInfluence(color)+ " "+ color+ " student, ");
        System.out.print(island.getColorInfluence(color)+ " "+ color+ " students, ");
    }
    System.out.println();
    if(island.getTowerColor().equals(TowerColor.EMPTY))
        System.out.println("There are no towers on this island");
    else if(island.getNumberOfTowers()>1)
        System.out.println("There are " +island.getNumberOfTowers() + " " +island.getTowerColor()+" towers on this island");
    else
        System.out.println("There is a " +island.getTowerColor()+" tower on this island");

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
    int chosenCloud;
    ArrayList<CloudTile> cloudsAvailable;
    //method to get info on clouds
    do {
        for(CloudTile cloud : cloudsAvailable)
            System.out.println("Cloud number " +cloud.getTileID()+ "contains the following students: " +cloud.getStudents().toString());
        System.out.println("Chose the cloud by writing its number: " +cloudsAvailable.toString() );
        chosenCloud = Integer.parseInt(scan.next());
        if(!cloudsAvailable.contains(chosenCloud))
            System.out.println("Invalid input");
    }while (!cloudsAvailable.contains(chosenCloud));
    cloudsAvailable.remove(chosenCloud);
    //listener.notify;
}

@Override
public void chooseMotherNatureMoves(int moves) {
    int chosenMove;
    do {
        System.out.print("Available moves: ");
        for(int i=1; i<=moves; i++)
            System.out.print("["+i+"]"+" ");
        System.out.print("Choose the number of moves for mother nature: ");
        chosenMove = Integer.parseInt(scan.next());
        if(chosenMove < 1 || chosenMove > moves)
            System.out.println("Invalid input");
    }while (chosenMove < 1 || chosenMove > moves);
    //listener.notifyListener(new MoveMotherNatureMessage(clientPlayer, chosenMove));
}

@Override
public void chooseMotherNaturePosition() {
    int lastIslandID;
    int ID;
    do {
        System.out.println("Choose the island of your interest: ");
        //method to get last island ID
        ID = Integer.parseInt(scan.next());
        if (ID < 0 || ID > lastIslandID)
            System.out.println("Invalid input");
    } while(ID < 0 || ID > lastIslandID);
    //listener.notifyListener()
}

public void getPlayerInfo() {
    String chosenPlayer;
    Player player;
    do {
        System.out.println("Choose which player you want to inspect by writing its name: " + players.toString());
        chosenPlayer = scan.next();
        if(!players.contains(chosenPlayer))
            System.out.println("Invalid input");
    }while(!players.toString().contains(chosenPlayer));
    //listener.notify(nickname)
    //method to retrieve player info
    System.out.println("Player " +chosenPlayer+ "has the following students in its entry room: " +player.getBoard().getEntryRoom().toString());
    System.out.println("Player " +chosenPlayer+ "has the following teachers in its hall: " +player.getBoard().getTeacher(); //change
}

@Override
public void printText(String text) {

}

@Override
public void askGameParam() {
    int numOfPlayers;
    String expertMode;
    boolean expert;
    do {
        System.out.print("Choose the number of players [2, 3, 4]: ");
        numOfPlayers = Integer.parseInt(scan.next());
        if(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4))
            System.out.println("Invalid parameter");
    }while(!(numOfPlayers == 2 || numOfPlayers == 3 || numOfPlayers == 4));

    do {
        System.out.print("Do you want to play in expert mode? [Y/N] ");
        expertMode = scan.next();
        if(!(expertMode.contains("Y") || expertMode.contains("N")))
            System.out.println("Invalid parameter");
    }while(!(expertMode.contains("Y") || expertMode.contains("N")));

    if(expertMode.contains("Y") && !expertMode.contains("N"))
        expert = true;

}
*/
    @Override
    public void connectionRequest(HashMap<String, String> connectionInfo) {

    }

    @Override
    public void sendSelectedID(ArrayList<Integer> ID) {
        int chosenID;
        do {
            System.out.println("Choose one from the available objects: " +ID.toString());
            chosenID = Integer.parseInt(scan.next());
            if(!ID.contains(chosenID))
                System.out.println("Invalid input");
        } while (!ID.contains(chosenID));
        int finalChosenID = chosenID;
        notifyListener(list -> list.sendSelectedID(finalChosenID));
    }

    @Override
    public void selectStudent(ArrayList<Color> students) {
        String color;
        Color finalColor;
        do {
            System.out.println("Choose the color of the student you want to move: " +students.toString());
            color = scan.next().toUpperCase(Locale.ROOT);
            if(!(color.contains("GREEN") || color.contains("PINK") || color.contains("RED") || color.contains("YELLOW") || color.contains("BLUE")));
            System.out.println("Invalid parameter");
        } while(!(color.contains("GREEN") || color.contains("PINK") || color.contains("RED") || color.contains("YELLOW") || color.contains("BLUE")));

        switch (color.toUpperCase(Locale.ROOT)) {
            case "RED": finalColor = Color.RED;
                break;
            case "YELLLOW": finalColor = Color.YELLOW;
                break;
            case "PINK": finalColor = Color.PINK;
                break;
            case "GREEN": finalColor = Color.GREEN;
                break;
            case "BLUE": finalColor = Color.BLUE;
                break;
        }
        Color finalColor1 = finalColor;
        notifyListener(list -> list.selectStudent(finalColor1));
    }

    @Override
    public void catchAction(Message receivedMessage) {

    }

    @Override
    public void chooseTowerColor(ArrayList<TowerColor> availableColors) {
        String towerColor;
        TowerColor finalColor;
        Boolean valid = false;
        do {
            System.out.println("Choose your tower color: " +availableColors.toString());
            towerColor = scan.next().toUpperCase(Locale.ROOT);
            if(availableColors.size() == 3) {
                if(towerColor.contains("WHITE") || towerColor.contains("BLACK") || towerColor.contains("GREY"));
                    valid = true;
            }
            else if(availableColors.size() == 2) {
                if(towerColor.contains("WHITE") || towerColor.contains("BLACK"));
                    valid = true;
            }
            else
                System.out.println("Invalid parameter");
        } while(!valid);

        switch (towerColor.toUpperCase(Locale.ROOT)) {
            case "BLACK": finalColor = TowerColor.BLACK;
                break;
            case "WHITE": finalColor = TowerColor.WHITE;
                break;
            case "GRAY": finalColor = TowerColor.GRAY;
                break;
        }
        TowerColor finalColor1 = finalColor;
        notifyListener(list -> list.chooseTowerColor(finalColor1));
    }

    @Override
    public void chooseCardDeck(ArrayList<DeckType> decksAvailable) {
        DeckType deck = null;
        String deckChosen;
        do{
            System.out.print("Choose your deck of cards: " + this.decksAvailable);
            deckChosen = scan.next().toUpperCase(Locale.ROOT);
            if(!(this.decksAvailable.toString().contains(deckChosen)))
                System.out.println("Invalid input");
        }while(!(this.decksAvailable.toString().contains(deckChosen)));

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
        DeckType finalDeck = deck;
        notifyListener(list -> list.chooseCardDeck(finalDeck));
    }

    @Override
    public void chooseDestination() {
        String destination;
        do {
            System.out.println("Choose the destination of the student: [BOARD], [ISLAND]");
            destination = scan.next().toUpperCase(Locale.ROOT);
            if(!(destination.toUpperCase().equals("BOARD") || destination.toUpperCase().equals("ISLAND")))
                System.out.println("Invalid input");
        } while (!(destination.toUpperCase().equals("BOARD") || destination.toUpperCase().equals("ISLAND")));

        String finalDestination = destination;
        notifyListener(list -> list.chooseDestination(finalDestination));
    }

    @Override
    public void getPlayerInfo(ArrayList<String> players) {
        String player;
        do {
            System.out.println("Choose the player you want to get info on: " +players.toString());
            player = scan.next();
            if(player.equals("") || !players.toString().contains(player))
                System.out.println("Invalid input");
        } while (player.equals(""));

        String finalPlayer = player;
        notifyListener(list -> list.getPlayerInfo(finalPlayer));
    }

    @Override
    public void remainingTowerAndDeck(ArrayList<TowerColor> remainingTowers, ArrayList<DeckType> remainingDecks) {

    }

    @Override
    public void disconnect() {

    }
}


