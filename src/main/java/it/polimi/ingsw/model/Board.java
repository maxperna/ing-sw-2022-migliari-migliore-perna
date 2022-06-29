package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import org.jetbrains.annotations.TestOnly;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the player board, handling all the movements of the students from the outside hall to the
 * island or the inner hall or from the cloud tile to the outside hall
 *
 * @author Massimo
 */
public class Board implements Serializable, StudentManager {

    private final int maxStudentHall;
    private final TowerColor towerColor;
    transient private final Player owner;
    private final Map<Color, Integer> diningRoom;      //list of student for each color inside the main hall
    private final Map<Color, Boolean> teachers;       //map to signal the presence of a teacher on the board
    transient private final Game currentGame;
    transient private final PropertyChangeSupport support;
    private Integer numberOfTowers;  //number of towers on the board
    transient private Player teamMate;
    private ArrayList<Color> entryRoom;    //list of student in the outer room


    /**
     * Default constructor
     * @param currentGame is the instance of the game
     * @param owner is the board player
     * @param towerColor is the color of the towers linked to this board
     */
    public Board(Game currentGame, Player owner, TowerColor towerColor) {

        this.maxStudentHall = currentGame.MAX_STUDENTS_ENTRANCE;
        this.numberOfTowers = currentGame.MAX_NUM_OF_TOWERS;
        this.towerColor = towerColor;
        this.diningRoom = new HashMap<>();
        this.teachers = new HashMap<>();

        this.currentGame = currentGame;
        this.owner = owner;

        try {
            this.entryRoom = currentGame.getPouch().randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }

        for (Color color : Color.values())
            this.diningRoom.put(color, 0);

        for (Color color : Color.values())
            this.teachers.put(color, false);

        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Getter
     * @return the students inside the entry room
     */
    public ArrayList<Color> getEntryRoom() {
        return entryRoom;
    }

    /**
     * Method used to get the number of the remaining towers
     * @return the number of the remaining towers
     */
    public Integer getNumOfTowers() {
        return numberOfTowers;
    }

    /**
     * Method to get the presence of a teacher of a given on the board
     *
     * @param color color of the requested teacher
     */
    public boolean getTeacher(Color color) {
        return teachers.get(color);
    }

    /**
     * Method to add a new teacher to the board
     *
     * @param color color of the teacher to add
     */
    public void addTeachers(Color color) {
        teachers.put(color, true);
    }

    /**
     * Method to remove a teacher from the board
     *
     * @param color color of the teacher to remove
     */
    public void removeTeacher(Color color) {
        teachers.put(color, false);
    }

    /**
     * Method to move a student from the outer hall to the internal one
     *
     * @param color color of the student to move
     * @throws NotOnBoardException exception thrown if it's tried to move a not existent student
     */
    public void moveEntryToDiningRoom(Color color) throws NotOnBoardException, NotEnoughSpace {
        if (!entryRoom.contains(color)) throw new NotOnBoardException();
        if (diningRoom.get(color) == 10) throw new NotEnoughSpace();
        else {
            entryRoom.remove(color);
            diningRoom.put(color, diningRoom.get(color) + 1);       //add a student of a color after removing it
            //Coin add if on 3,6,9th space
            if (diningRoom.get(color) % 3 == 0 && diningRoom.get(color) != 0 && currentGame.EXP_MODE) {
                int oldValue = owner.getNumOfCoin();
                currentGame.coinHandler(owner, 1);
                support.firePropertyChange("UpdateCoin-" + owner.getNickname(), oldValue, owner.getNumOfCoin());
            }
        }

    }

    /**
     * Method to move a student from the outer hall to an island on the game field
     *
     * @param color color of the student to move
     * @throws NotOnBoardException exception thrown if it's tried to move a not existent student
     */
    public void moveToIsland(Color color, int nodeID) throws NotOnBoardException, IllegalMove {
        if (!entryRoom.contains(color)) throw new NotOnBoardException();
        else {
            try {
                entryRoom.remove(color);
                currentGame.getGameField().addStudent(nodeID, color);

            } catch (IllegalMove e) {
                entryRoom.add(color);
                throw new IllegalMove();
            }
        }

    }

    /**
     * Method to remove students from the outside room
     *
     * @param colorToRemove array list of student to move
     * @return the student removed
     */
    public ArrayList<Color> moveFromEntryRoom(ArrayList<Color> colorToRemove) throws NotOnBoardException {
        if (!entryRoom.containsAll(colorToRemove)) throw new NotOnBoardException();
        else {
            for (Color colorToRM : colorToRemove)
                entryRoom.remove(colorToRM);
            return colorToRemove;
        }
    }

    /**
     * Method to remove students from the outside room
     *
     * @param colorToRemove array list of student to move
     * @return the student removed
     */
    public ArrayList<Color> moveFromDiningRoom(ArrayList<Color> colorToRemove) throws NotOnBoardException {
        ArrayList<Color> colorToReturn = new ArrayList<>();
        for (Color color : colorToRemove) {
            if (diningRoom.get(color) == 0) throw new NotOnBoardException();
            else {
                diningRoom.put(color, diningRoom.get(color) - 1);
                colorToReturn.add(color);
            }
        }
        return colorToReturn;
    }

    /**
     * Method to add students on the outside room
     *
     * @param studentsToAdd list of student to add
     */
    public void addStudentsEntryRoom(ArrayList<Color> studentsToAdd) throws NotEnoughSpace {
        if (entryRoom.size() + studentsToAdd.size() > maxStudentHall) throw new NotEnoughSpace();
        else {
            entryRoom.addAll(studentsToAdd);
        }
        support.firePropertyChange("UpdateBoard", false, true);
    }

    /**
     * Method to add student to the inside hall from external sources
     *
     * @param studentsToAdd array list of students to add
     */
    public void addStudentsDiningRoom(ArrayList<Color> studentsToAdd) throws NotEnoughSpace {
        for (Color color : studentsToAdd) {
            //maximum students on the dining room
            int MAX_DIM_INSIDE = 10;
            if (diningRoom.get(color) + 1 > MAX_DIM_INSIDE)
                throw new NotEnoughSpace();
            else
                diningRoom.put(color, diningRoom.get(color) + 1);
        }
    }

    /**
     * Method to move a tower from the hall to an island
     *
     * @throws EndGameException exception thrown if there aren't any tower, make the game end
     */
    public TowerColor moveTower() throws EndGameException {

        numberOfTowers--;
        if (numberOfTowers == 0) throw new EndGameException("Out of towers");

        return getTowerColor();

    }

    /**
     * Method to add a tower to the hall
     */
    public void addTower() {
        numberOfTowers++;
    }

    /**
     * Method used to get information on the teachers owned by the player
     * @return a map containing all the teachers owned
     */
    public Map<Color, Boolean> getTeachers() {
        return teachers;
    }

    /**
     * Method used to get the tower color
     * @return the tower color of this board
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * Method used to get the nickname of the teammate
     * @return the nickname of the teammate
     */
    public Player getTeamMate() {
        return teamMate;
    }

    /**
     * Method to set the teammate on the board only if it has not been set yet and to add reference to the common tower
     * in 4 players game
     *
     * @param teamMate players to set as teammate
     */
    public void setTeamMate(Player teamMate) {
        if (this.teamMate == null && currentGame.NUM_OF_PLAYERS == 4) {
            this.teamMate = teamMate;
            this.numberOfTowers = teamMate.getBoard().numberOfTowers;
        }
    }

    /**
     * Method used to set a propertyChangeListener on this board
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

    /**
     * Method used to get information on the students inside the dining room
     * @return a map containing all the students inside the dining room
     */
    public Map<Color, Integer> getDiningRoom() {
        return diningRoom;
    }

    /**
     * Return the num of student of a color in the dining room
     *
     * @param color color to check
     */
    @Override
    public int colorStudent(Color color) {
        return diningRoom.get(color);
    }


    @TestOnly
    public void addSingleStudent(Color student) {
        this.entryRoom.add(student);
    }

    @TestOnly
    /**Add a student pawn directly to dning room(TEST ONLY)*/
    public void addToDiningTest(Color student) {
        this.diningRoom.put(student, diningRoom.get(student) + 1);
    }

    /**
     * Method to get the max number of students inside the entry hall
     * @return the max number of students inside the entry hall
     */
    public int getMaxStudentHall() {
        return maxStudentHall;
    }

}
