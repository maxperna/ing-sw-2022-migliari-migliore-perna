package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.experts.ExpertCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * class used as an extension of Subject (the object that will send the notifies, old Observable), specific for the View, contains all methods used to interact to the view, uses lambdas to get access of methods
 */
public abstract class ViewSubject {

    protected final List<ViewListener> list = new ArrayList<>();

    public void addListener(ViewListener l) {
        list.add(l);
    }

    public void removeListener(ViewListener l) {
        list.remove(l);
    }

    public void addAllListeners(List<ViewListener> l) {
        list.addAll(l);
    }

    public void removeAllListeners(List<Listener> l) {
        list.removeAll(l);
    }

    protected void notifyListener(Consumer<ViewListener> lambda) {
        for(ViewListener list : list)
            lambda.accept(list);
    }
}
