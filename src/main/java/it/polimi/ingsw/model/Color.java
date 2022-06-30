package it.polimi.ingsw.model;

/**
 * Enum for the colors of Teacher and Student with ref to the pawn
 *
 * @author Miglia
 */
public enum Color {
    RED(1, "images/Scontornate/red_stud.png", "images/Scontornate/red_prof.png"),
    BLUE(4, "images/Scontornate/blue_stud.png", "images/Scontornate/blue_prof.png"),
    GREEN(0, "images/Scontornate/green_stud.png", "images/Scontornate/green_prof.png"),
    PINK(3, "images/Scontornate/pink_stud.png", "images/Scontornate/pink_prof.png"),
    YELLOW(2, "images/Scontornate/yellow_stud.png", "images/Scontornate/yellow_prof.png");

    private final String studImg;
    private final String teacherImg;
    private final int boardIndex;

    Color(int boardIndex, String studImg, String teacherImg) {
        this.boardIndex = boardIndex;
        this.studImg = studImg;
        this.teacherImg = teacherImg;
    }

    public String getTeacherImg() {
        return teacherImg;
    }

    public String getStudImg() {
        return studImg;
    }

    public int getBoardIndex() {
        return boardIndex;
    }
}
