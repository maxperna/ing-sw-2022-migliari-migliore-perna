package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.*;

/**Implementation of the player board, handling all the movements of the students from the outside hall to the
*island or the inner hall or from the cloud tile to the outside hall
* @author Massimo
*/
public class Board implements StudentManager {

    private final int hallDimension;       //maximum number of players in the external hall depending by the num of players
    private final int maxNumOfTowers;        //maximum number of towers depending by the number of players
    private TowerColor towerColor;      //color of the tower on the board
    private final UUID boardID;
    private ArrayList<Color> studentsOutside;    //list of student in the outer room
    private Map<Color,Integer> lectureHall;      //list of student for each color inside the main hall
    private Map<Color, Boolean> teachers;       //map to signal the presence of a teacher on the board
    private ArrayList<TowerColor> towers;  //number of towers on the board

    /**Private constructor used by the public factory, it is assigned a UUID to have a thread safe identifier
     * @param maxNumOfTowers maximun num of towers on the board depends on the num of players
     * @param hallDimension dimension of the outside hall, depends on the num of player
     */
    private Board(int maxNumOfTowers, int hallDimension){
        this.boardID = UUID.randomUUID();         //thread safe auto generated ID
        this.maxNumOfTowers = maxNumOfTowers;
        this.hallDimension = hallDimension;

    }

    /**Public method used to call the private constructor
     *  @param maxNumOfTowers maximun num of towers on the board depends on the num of players
     *   @param hallDimension dimension of the outside hall, depends on the num of player
     */
    public static Board createBoard(int maxNumOfTowers,int hallDimension){
        return new Board(maxNumOfTowers,hallDimension);
    }

    public UUID getBoardID(){
        return this.boardID;
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

        }
    }

    /**Method to move a student from the outer hall to an island on the gamefield
     * @param color color of the student to move
     * @exception NotOnBoardException exception thrown if it's tried to move an inexistent student
     */
    public void moveToIsland(Color color, IslandTile targetIsland) throws NotOnBoardException{
        if(!studentsOutside.contains(color)) throw new NotOnBoardException();
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

}
