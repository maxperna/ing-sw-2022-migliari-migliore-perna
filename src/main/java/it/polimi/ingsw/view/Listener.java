package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.Message;

/**
 * interface of a simple Listener (old Observer)
 */
public interface Listener {

    void update(Message message);

}

