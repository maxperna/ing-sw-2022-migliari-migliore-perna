package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

/**
 * interface of a simple Listener (old Observer)
 */
public interface Listener {

    void update(Message message);

}

