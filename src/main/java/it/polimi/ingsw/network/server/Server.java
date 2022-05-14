package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Server{
    ServerSocket serverSocket;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final GameController gameController;
    private final Map<ClientHandler, VirtualView> virtualViewMap;

    private final Object lock;

    public Server(GameController gameController){
        this.gameController = gameController;
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.lock = new Object();
    }

    public void addClient(String nickname, ClientHandler clientHandler){
        VirtualView newVW = new VirtualView(clientHandler);
        if(gameController.getGameState().equals(GameState.LOGIN)){
            if(gameController.checkNicknameValidity(nickname)){
                virtualViewMap.put(clientHandler,newVW);
                gameController.logInHandler(nickname,newVW);
            }
            else{
                //Can't access to game
                clientHandler.disconnect();
            }
        }
    }

    /**Method that handles the reception of a new message from the client
     * @param receivedMessage message sent by the server*/
    public void receivedMessage(Message receivedMessage){
        gameController.onMessageReceived(receivedMessage);
    }



}
