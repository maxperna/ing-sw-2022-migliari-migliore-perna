package it.polimi.ingsw.view;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * class used as an extension of Subject (the object that will send the notifies, old Observable), specific for the View, contains all methods used to interact to the view, uses lambdas to get access of methods
 */
public abstract class ViewSubject {

    ArrayList<ViewListener> list = new ArrayList<>();

    public void addListener(ViewListener l) {
        list.add(l);
    }

    public void removeListener(ViewListener l) {
        list.remove(l);
    }

    public void addAllListeners(ArrayList<ViewListener> l) {
        list.addAll(l);
    }

    public void removeAllListeners(ArrayList<Listener> l) {
        list.removeAll(l);
    }

    protected void notifyListener(Consumer<ViewListener> lambda) {
        for(ViewListener list : list)
            lambda.accept(list);
    }



}
