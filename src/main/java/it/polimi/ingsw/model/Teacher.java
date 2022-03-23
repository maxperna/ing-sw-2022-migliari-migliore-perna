package it.polimi.ingsw.model;

/**
 * Class Teacher
 * @author Miglia
 */
public class Teacher {

    private final Color color;

    /**
     * Constructor
     * @param color color of the teacher that will be created
     */
    public Teacher(Color color) {
        this.color = color;
    }

    /**
     * Getter
     * @return color of the teacher
     */
    public Color getColor() {
        return color;
    }
}
