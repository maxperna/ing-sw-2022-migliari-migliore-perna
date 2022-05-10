package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Server {
    private final GameController gameController;
    //private final Map<String,ClientHandler>

    public final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    private Object locker;   //object to lock the synchro method on

    public Server(GameController gameController){
        this.gameController = gameController;
        //map = new(synchronizedMap)
        this.locker = new Object();
    }

    public void addClient(){}

    public void removeClient(){}

    public void receiveMessage(){}


}
