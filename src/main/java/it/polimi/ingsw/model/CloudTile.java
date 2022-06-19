package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Cloud Tile, one for each player, contains MAX 3 students
 * @author Alessio Migliore
 */
public class CloudTile implements Serializable {

    private final int tileID;
    private ArrayList<Color> students;
    final transient private PropertyChangeSupport support;

    /**
     * constructor
     */
    public CloudTile(int ID){
        this.tileID = ID;
        this.students = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * @return an Arraylist of Color that represents students on the cloud
     */
    public ArrayList<Color> getStudents(){
            return students;
    }

    /**
     * @throws EmptyCloudException when there are no students on the cloud
     */
    public synchronized ArrayList<Color> moveStudents() throws EmptyCloudException{
        if(students.isEmpty())
            throw new EmptyCloudException();
        else {
            ArrayList<Color> studentsReturned = new ArrayList<>(students);                      //creates an ArrayList in which all students are copied
            students.clear();                                                                        //empties the ArrayList students
            return studentsReturned;
        }
    }

    /**
     * method that returns the ID of the cloud
     * @return cloud ID
     */
    public int getTileID (){
        return this.tileID;
    }

    public void setStudents(ArrayList<Color> students) {
        this.students = students;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
