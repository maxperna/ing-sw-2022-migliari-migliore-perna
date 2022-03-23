package it.polimi.ingsw.model;

/**
 * Class Student
 * @author Miglia
 */
public class Student {
    private final Color color;

    /**
     * Constructor
     * @param color color of the student that will be created
     */
    public Student(Color color) {
        this.color = color;
    }

    /**
     * Getter
     * @return color of the student
     */
    public Color getColor() {
        return color;
    }
}
