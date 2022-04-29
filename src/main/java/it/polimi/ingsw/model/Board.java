package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughSpace;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.NotOnBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**Implementation of the player board, handling all the movements of the students from the outside hall to the
*island or the inner hall or from the cloud tile to the outside hall
* @author Massimo
*/
public class Board implements StudentManager {

    private final int maxStudentHall;
    private final int maxTowers;
    private final TowerColor towerColor;
    private final UUID gameID;
    private ArrayList<Color> studentsOutside;    //list of student in the outer room
    private final Map<Color,Integer> lectureHall = new HashMap<>();      //list of student for each color inside the main hall
    private final Map<Color, Boolean> teachers = new HashMap<>();       //map to signal the presence of a teacher on the board
    private ArrayList<TowerColor> towers;  //number of towers on the board


    public Board(UUID gameID, int maxStudentHall, int maxTowers, TowerColor towerColor){

        this.gameID = gameID;         //thread safe auto generated ID
        this.maxStudentHall = maxStudentHall;
        this.maxTowers = maxTowers;
        this.towerColor = towerColor;

        try {
            this.studentsOutside = Pouch.getInstance(gameID).randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }

        //...la lista delle torri di ogni giocatore viene popolata
        ArrayList <TowerColor> listOfTowers = new ArrayList <>();
        for (int j = 0; j < maxTowers; j++) {
            listOfTowers.add(towerColor);
        }
        this.towers = listOfTowers;

    }

    public Board(UUID gameID, int maxStudentHall, int maxTowers, TowerColor towerColor, ArrayList<TowerColor> towers){

        this.gameID = gameID;         //thread safe auto generated ID
        this.maxStudentHall = maxStudentHall;
        this.maxTowers = maxTowers;
        this.towerColor = towerColor;
        this.towers = towers;

        try {
            this.studentsOutside = Pouch.getInstance(gameID).randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Color> getStudentsOutside(){
        return studentsOutside;
    }
    //da modificare
    public int getNumOfTowers() {
        return towers.size();
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
    public void moveToIsland(Color color, IslandTile targetIsland) throws NotOnBoardException{
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
        if(towers.size() == 0) throw new EndGameException("Out of towers");
        else{
            towers.remove(towerColor);
            return this.towerColor;
        }
    }

    /**Method to add a tower to the hall
     */
    public void addTower(){
        towers.add(towerColor);
    }

    /**Method to move student from the cloud tile to outer hall
     */
    public void setStudentsOutside(ArrayList<Color> studentsOutside) {
        this.studentsOutside = studentsOutside;
    }

    public ArrayList <TowerColor> getTowers() {
        return towers;
    }

    public void setTowers(ArrayList <TowerColor> towers) {
        this.towers = towers;
    }

    public int colorStudent(Color color){
        return 0;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }
}
