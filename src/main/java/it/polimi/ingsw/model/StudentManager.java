package it.polimi.ingsw.model;

/**
 * Student Manager Interface
 * @author Miglia
 */
public interface StudentManager {
    /**
     * @param color color of the students you want to count
     * @return the number of students with the same color passed as parameter
     */
    public int colorStudent(Color color);
}
