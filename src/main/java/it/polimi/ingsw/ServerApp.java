package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.Server_Socket;


/**
 * Hello world!
 */
public class ServerApp {
    public static void main(String[] args) {
        int serverPort = 13000;

        for (int i = 0; i < args.length; i++) {
            if (args.length > 2 && (args[i].equals("--port")) || (args[i].equals("-p"))) {
                try {
                    serverPort = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    Server.LOGGER.warning("Port not valid, starting server on default port #13000");
                }
            }
        }

        GameController gameController = new GameController();
        Server server = new Server(gameController);

        Server_Socket serverSocket = new Server_Socket(server, serverPort);
        Thread serverThread = new Thread(serverSocket, "Server");
        serverThread.start();
    }
}
