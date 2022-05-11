package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Socket implements Runnable {
    private final int port;     //port of the server
    private final Server server;
    private ServerSocket serverSocket;

    public Server_Socket(Server server, int port) {
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            Server.LOGGER.info("Server started on port"+port);



        } catch (IOException e) {
            Server.LOGGER.severe("Failed start server");
        }

        while(!Thread.currentThread().isInterrupted()){
            try{
                Socket client = serverSocket.accept();
                //aggiungere clientHandler e nuovo thread e thread.start
            }catch(IOException e){
                Server.LOGGER.severe("Connection dropped");
            }
        }
        //Accettazione connesioni client
    }
}
