package it.polimi.ingsw.observer;

import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

/**
 * old Observable, contains the methods used to set up Listeners and updates them through a Message that can be read, you can change it since i haven't used it yet, can be used in substitution of lambdas
 */
public class Subject {

    ArrayList<Listener> list = new ArrayList<>();

    public void addListener(Listener l) {
        list.add(l);
    }

    public void removeListener(Listener l) {
        list.remove(l);
    }

    public void addAllListeners(ArrayList<Listener> l) {
        list.addAll(l);
    }

    public void removeAllListeners(ArrayList<Listener> l) {
        list.removeAll(l);
    }

    public void notifyListener(Message message) {
        for (Listener l : list)
            l.update(message);
    }

}
