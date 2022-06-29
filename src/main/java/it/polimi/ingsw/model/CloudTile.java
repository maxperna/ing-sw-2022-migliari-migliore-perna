package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Cloud Tile, one for each player, contains max 3 students
 *
 * @author Alessio Migliore
 */
public class CloudTile implements Serializable {

    private final int tileID;
    final transient private PropertyChangeSupport support;
    private ArrayList<Color> students;

    /**
     * Default constructor
     */
    public CloudTile(int ID) {
        this.tileID = ID;
        this.students = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Getter
     * @return an Arraylist of Color that represents students on the cloud
     */
    public ArrayList<Color> getStudents() {
        return students;
    }

    /**
     * Method used to add 3 students to the cloud tile
     * @param students are the students that will be moved to the cloud tile
     */
    public void setStudents(ArrayList<Color> students) {
        this.students = students;
    }

    /**
     * Method used to remove the students on a cloud tile
     * @throws EmptyCloudException when there are no students on the cloud
     * @return the students removed
     */
    public synchronized ArrayList<Color> moveStudents() throws EmptyCloudException {
        if (students.isEmpty())
            throw new EmptyCloudException();
        else {
            ArrayList<Color> studentsReturned = new ArrayList<>(students);                      //creates an ArrayList in which all students are copied
            students.clear();                                                                        //empties the ArrayList students
            return studentsReturned;
        }
    }

    /**
     * Method that returns the ID of the cloud
     *
     * @return cloud ID
     */
    public int getTileID() {
        return this.tileID;
    }

    /**
     * Method used to set a propertyChangeListener on this cloud
     * @param listener is the listener that will notify all the changes
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove a listener
     * @param listener listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
