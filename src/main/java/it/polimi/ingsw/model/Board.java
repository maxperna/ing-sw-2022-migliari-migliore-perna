package it.polimi.ingsw.model;

import it.polimi.ingsw.gameField.Node;
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
    private final int MAX_DIM_INSIDE = 10;    //maximum students on the dining room
    private final int maxTowers;
    private final TowerColor towerColor;
    private Integer numberOfTowers;  //number of towers on the board
    private Player teamMate;
    private ArrayList<Color> entryRoom;    //list of student in the outer room
    private final Map<Color,Integer> diningRoom = new HashMap<>();      //list of student for each color inside the main hall
    private final Map<Color, Boolean> teachers = new HashMap<>();       //map to signal the presence of a teacher on the board

    private final Game currentGame;



    public Board(Game currentGame, int maxStudentHall, Integer numberOfTowers, TowerColor towerColor){

        this.maxStudentHall = maxStudentHall;
        this.maxTowers = numberOfTowers;
        this.towerColor = towerColor;
        this.numberOfTowers = numberOfTowers;
        this.currentGame = currentGame;

        try {
            this.entryRoom = currentGame.getPouch().randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }

    }

    public Board(Game currentGame, int maxStudentHall, Integer numberOfTowers, TowerColor towerColor, Player teamMate){

        this.maxStudentHall = maxStudentHall;
        this.maxTowers = numberOfTowers;
        this.towerColor = towerColor;
        this.numberOfTowers = numberOfTowers;
        this.teamMate = teamMate;
        this.currentGame = currentGame;

        try {
            this.entryRoom = currentGame.getPouch().randomDraw(maxStudentHall);
        } catch (NotEnoughStudentsException e) {
            e.printStackTrace();
        }


    }
    public ArrayList<Color> getEntryRoom(){
        return entryRoom;
    }
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
    public void moveEntryToDiningRoom(Color color) throws NotOnBoardException,NotEnoughSpace{
        if(!entryRoom.contains(color)) throw new NotOnBoardException();
        if(diningRoom.get(color)==10) throw new NotEnoughSpace();
        else{
            entryRoom.remove(color);
            diningRoom.put(color, diningRoom.get(color)+1);       //add a student of a color after removing it
            //Coin add if on 3,6,9th space
            if(diningRoom.get(color)%3==0 && diningRoom.get(color)!=0){
                Player playerToAdd = currentGame.getBoardAssignations().get(this);
                currentGame.coinHandler(playerToAdd,1);
            }

        }
    }

    /**Method to move a student from the outer hall to an island on the gamefield
     * @param color color of the student to move
     * @exception NotOnBoardException exception thrown if it's tried to move an inexistent student
     */
    public void moveToIsland(Color color, Node targetIsland) throws NotOnBoardException{
        if(!entryRoom.contains(color)) throw new NotOnBoardException();
        else{
            entryRoom.remove(color);
            targetIsland.addStudent(color);
        }

    }

    /**Method to remove students from the outside room
     * @param colorToRemove array list of student to move
     * @return the student removed
     * */
    public ArrayList<Color> moveFromEntryRoom(ArrayList<Color> colorToRemove) throws NotOnBoardException{
        if(!entryRoom.containsAll(colorToRemove)) throw new NotOnBoardException();
        else{
            entryRoom.removeAll(colorToRemove);
            return colorToRemove;
        }
    }

    /**Method to remove students from the outside room
     * @param colorToRemove array list of student to move
     * @return the student removed
     * */
    public ArrayList<Color> moveFromDiningRoom(ArrayList<Color> colorToRemove) throws NotOnBoardException{
        ArrayList<Color>  colorToReturn = new ArrayList<>();
        for(Color color:colorToRemove) {
            if (diningRoom.get(color)==0) throw new NotOnBoardException();
            else {
                diningRoom.put(color, diningRoom.get(color)-1);
                colorToReturn.add(color);
            }
        }
        return colorToReturn;
    }

    /**Method to add students on the outside room
     * @param studentsToAdd list of student to add
     * */
    public void addStudentsEntryRoom(ArrayList<Color> studentsToAdd) throws NotEnoughSpace {
        if(entryRoom.size()+ studentsToAdd.size()> maxStudentHall) throw new NotEnoughSpace();
        else{
            entryRoom.addAll(studentsToAdd);
        }
    }

    /**Method to add studnet to the inside hall from external sources
     * @param studentsToAdd array list of students to add*/
    public void addStudentsDiningRoom(ArrayList<Color> studentsToAdd) throws NotEnoughSpace{
        for(Color color:studentsToAdd){
            if(diningRoom.get(color)+1 > MAX_DIM_INSIDE)
                throw new NotEnoughSpace();
            else
                diningRoom.put(color,diningRoom.get(color)+1);
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
    public void setEntryRoom(ArrayList<Color> entryRoom) {
        this.entryRoom = entryRoom;
    }

    public int colorStudent(Color color){
        return 0;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }
}