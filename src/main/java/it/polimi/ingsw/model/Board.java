package it.polimi.ingsw.model;

import it.polimi.ingsw.circularLinkedList.Node;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.NotOnBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**Implementation of the player board, handling all the movements of the students from the outside hall to the
*island or the inner hall or from the cloud tile to the outside hall
* @author Massimo
*/
public class Board implements StudentManager {

    private final int maxStudentHall;
    private final int maxTowers;
    private final TowerColor towerColor;
    private Integer numberOfTowers;  //number of towers on the board
    private Player teamMate;
    private ArrayList<Color> studentsOutside;    //list of student in the outer room
    private final Map<Color,Integer> lectureHall = new HashMap<>();      //list of student for each color inside the main hall
    private final Map<Color, Boolean> teachers = new HashMap<>();       //map to signal the presence of a teacher on the board



    public Board(Pouch pouch, int maxStudentHall, Integer numberOfTowers, TowerColor towerColor){

        this.maxStudentHall = maxStudentHall;
        this.maxTowers = numberOfTowers;
        this.towerColor = towerColor;
        this.numberOfTowers = numberOfTowers;

        try {
            this.studentsOutside = pouch.randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }

    }

    public Board(Pouch pouch, int maxStudentHall, Integer numberOfTowers, TowerColor towerColor, Player teamMate){

        this.maxStudentHall = maxStudentHall;
        this.maxTowers = numberOfTowers;
        this.towerColor = towerColor;
        this.numberOfTowers = numberOfTowers;
        this.teamMate = teamMate;

        try {
            this.studentsOutside = pouch.randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Color> getStudentsOutside(){
        return studentsOutside;
    }
    //da modificare
    public Integer getNumOfTowers() {
        return numberOfTowers;
    }

    /**Method to get the presence of a teacher of a given on the board
     * @param color color of the requested teacher
     */
    public boolean getTeacher(Color color){
        return teachers.get(color);
    }

    /**Method to add a new teacher to the board
     * @param color color of the teacher to add
     */
    public void addTeachers(Color color){
        teachers.put(color,true);
    }

    /**Method to remove a teacher from the board
     * @param color color of the teacher to remove
     */
    public void removeTeacher(Color color){
        teachers.put(color, false);
    }

    /**Method to move a student from the outer hall to the internal one
     * @param color color of the student to move
     * @exception NotOnBoardException exception thrown if it's tried to move an inexistent student
     */
    public void moveInside(Color color) throws NotOnBoardException{
        if(!studentsOutside.contains(color)) throw new NotOnBoardException();
        else{
            studentsOutside.remove(color);
            lectureHall.put(color,lectureHall.get(color)+1);       //add a student of a color after removing it
            /*Coin add if on 3,6,9th space
            if(lectureHall.get(color)%3==0 && lectureHall.get(color)!=0){
                observer for add coin
            }*/

        }
    }

    /**Method to move a student from the outer hall to an island on the gamefield
     * @param color color of the student to move
     * @exception NotOnBoardException exception thrown if it's tried to move an inexistent student
     */
    public void moveToIsland(Color color, Node targetIsland) throws NotOnBoardException{
        if(!studentsOutside.contains(color)) throw new NotOnBoardException();
        else{
            studentsOutside.remove(color);
            targetIsland.addStudent(color);
        }

    }

    public ArrayList<Color> moveFromOutsideRoom(ArrayList<Color> colorToRemove) throws NotOnBoardException{
        if(!studentsOutside.containsAll(colorToRemove)) throw new NotOnBoardException();
        else{
            studentsOutside.removeAll(colorToRemove);
            return colorToRemove;
        }
    }

    /**Method to add students on the outside room
     * @param studentToAdd list of student to add
     * */
    public void addStudentOutsideRoom(ArrayList<Color> studentToAdd) throws NotEnoughSpace {
        if(studentsOutside.size()+ studentToAdd.size()> maxStudentHall) throw new NotEnoughSpace();
        else{
            studentsOutside.addAll(studentToAdd);
        }
    }

    /**Method to move a tower from the hall to an island
     * @exception EndGameException exception thrown if there aren't any tower, make the game end
     */
    public TowerColor moveTower() throws EndGameException{
        if(numberOfTowers == 0) throw new EndGameException("Out of towers");
        else{
            numberOfTowers --;
            return getTowerColor();
        }
    }

    /**Method to add a tower to the hall
     */
    public void addTower(){
        numberOfTowers++;
    }

    /**Method to move student from the cloud tile to outer hall
     */
    public void setStudentsOutside(ArrayList<Color> studentsOutside) {
        this.studentsOutside = studentsOutside;
    }

    public int colorStudent(Color color){
        return 0;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }
}
