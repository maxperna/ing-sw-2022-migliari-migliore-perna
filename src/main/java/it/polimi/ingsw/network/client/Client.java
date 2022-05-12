package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.util.Observable;
import java.util.logging.Logger;

/**Interface defying client actions
 * @author Massimo*/
public abstract class Client extends Observable {
    //Logger creation to catch log event
    public static final Logger LOGGER =Logger.getLogger(Client.class.getName());

    public abstract void sendMessage(Message messageToSend);

    public abstract void receiveMessage();

    public abstract void disconnect();
}
