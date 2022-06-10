package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.server_messages.ErrorMessage;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Server{

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final GameController gameController;
    private final Map<ClientHandler, VirtualView> virtualViewMap;
    private final Object lock;
    private boolean firstConnection;

    public Server(GameController gameController){
        this.gameController = gameController;
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.lock = new Object();
        this.firstConnection = true;
    }

    public synchronized void addClient(String nickname, ClientHandler clientHandler){
        VirtualView newVW = new VirtualView(clientHandler);
        if(gameController.getGameState().equals(GameState.LOGIN)){
            if(gameController.checkNicknameValidity(nickname) && (firstConnection || gameController.getGame()!=null)){
                firstConnection = false;
                virtualViewMap.put(clientHandler,newVW);
                gameController.logInHandler(nickname,newVW);
            }
            else{
                //Can't access to game
                if(gameController.getGame()==null)
                    clientHandler.sendMessage(new ErrorMessage("Wait for game creation before inserting nick", ErrorType.LOGIN_ERROR));
                else
                    clientHandler.sendMessage(new ErrorMessage("NickName already exists", ErrorType.LOGIN_ERROR));

            }
        }
    }

    /**Method that handles the reception of a new message from the client
     * @param receivedMessage message sent by the server*/
    public void receivedMessage(Message receivedMessage){
        gameController.onMessageReceived(receivedMessage);
    }

}
