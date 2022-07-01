package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class that controls the panel showing the player's board
 */
public class BoardInfoSceneController extends ViewSubject implements GenericSceneController, Initializable {

    Map<String, Board> boardMap;
    @FXML
    AnchorPane anchorPane;
    @FXML
    GridPane container;
    @FXML
    Label label00;
    @FXML
    Label label01;
    @FXML
    Label label10;

    /**
     * Default boardScene controller
     * @param boardMap is a map containing all the available boards
     */
    public BoardInfoSceneController(Map<String, Board> boardMap) {
        this.boardMap = boardMap;
    }

    /**
     * Method used to initialize the panel showing the player's board
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Pair<String, Board>> pairList = new ArrayList<>();

        for (String nickName : boardMap.keySet())
            pairList.add(new Pair<>(nickName, boardMap.get(nickName)));

        if(pairList.size() >= 1) {
            container.lookup("#board2").setVisible(false);
            container.lookup("#board3").setVisible(false);

            AnchorPane node = (AnchorPane) container.lookup("#board1");
            populateBoard(pairList.get(0).getValue(), node);
            label00.setText(pairList.get(0).getKey());
        }
        if(pairList.size() >= 2) {
            container.lookup("#board2").setVisible(true);
            container.lookup("#board3").setVisible(false);

            AnchorPane node = (AnchorPane) container.lookup("#board2");
            populateBoard(pairList.get(1).getValue(), node);
            label01.setText(pairList.get(1).getKey());
        }
        if(pairList.size() == 3) {
            container.lookup("#board3").setVisible(true);

            AnchorPane node = (AnchorPane) container.lookup("#board3");
            populateBoard(pairList.get(2).getValue(), node);
            label10.setText(pairList.get(2).getKey());
        }
    }

    /**
     * Method used to fill the board with the students that are set based on the model board
     * @param board is the player's board
     * @param pane is the pane that will be used to show the board
     */
    public void populateBoard(Board board, AnchorPane pane){

        populateEntry(board.getEntryRoom(), (GridPane) pane.lookup("#entryRoom"));
        createTowers(board.getTowerColor(),board.getNumOfTowers(), (GridPane) pane.lookup("#towerSpace"));
        setTeachers(board.getTeachers(), (GridPane) pane.lookup("#professorSpace"));
        populateDiningRoom(board.getDiningRoom(), (GridPane) pane.lookup("#diningRoom"));
    }

    /**
     * Method called by populateBoard() to set the students inside the entry room
     * @param entry is an arrayList containing all the students that will be placed on the entry room
     * @param room is the gridPane dividing the pane in different zones, one per student inside the entry room
     */
    private void populateEntry(ArrayList<Color> entry, GridPane room) {
        int k =0;
        int bound = entry.size();
        room.getChildren().clear();
        if(bound%2 == 1) {
            bound = bound / 2;
            bound = bound + 1;
        }
        else
            bound = bound/2;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bound; j++) {
                if(!(i==0 && j==0)) {
                    Color colorToAdd = entry.get(k);
                    ImageView student = studentGenerator(colorToAdd);
                    room.add(student, i, j);
                    k++;
                }
            }
        }
    }

    /**
     * Method called by populateBoard() to set the students inside the dining room
     * @param studentMap is a map containing all the students that are placed inside the dining room
     * @param diningRoom is the gridPane dividing the dining room in different zones, one per student inside the dining room
     */
    private void populateDiningRoom(Map<Color, Integer> studentMap, GridPane diningRoom) {
        for(Color color : studentMap.keySet()) {
            for(int i = 0; i < studentMap.get(color); i ++) {
                ImageView student = studentGenerator(color);
                diningRoom.add(student, i, color.getBoardIndex());
            }
        }
    }

    /**
     * Method called by populateBoard() to set all the available towers
     * @param tower is the color associated to this board's towers
     * @param numOfTowers is the number of available towers
     * @param towerSpace is the gridPane dividing the pane into different zones, one per tower
     */
    private void createTowers(TowerColor tower,int numOfTowers, GridPane towerSpace){
        String towerImage = tower.getTowerImg();
        for(int i=0;i<2;i++){
            for(int j=0;j<numOfTowers/2;j++){
                ImageView towerIMGV = new ImageView(towerImage);
                towerIMGV.setFitWidth(15);
                towerIMGV.setFitHeight(50);
                towerSpace.add(towerIMGV,i,j);
            }
        }
    }

    /**
     * Method called by populateBoard() to set all the owned teachers
     * @param teachers is a map containing information about all the owned teachers
     * @param professorSpace is the gridPane dividing the teacher's area in different zones, one per teacher
     */
    private void setTeachers(Map<Color,Boolean> teachers, GridPane professorSpace){

        for(Color color:teachers.keySet() ) {
            if(teachers.get(color)) {
                ImageView teacherIMG = generateTeacher(color);
                professorSpace.add(teacherIMG, 0, color.getBoardIndex());
            }
        }
    }

    /**
     * Method called by populateDiningRoom() and populateEntryRoom(), used to print the students inside the board
     * @param color is the color of the student that will be printed
     * @return the modified imageView
     */
    private ImageView studentGenerator(Color color){
        ImageView image = new ImageView(color.getStudImg());
        image.setFitWidth(25);
        image.setFitHeight(25);
        image.setId(color.toString());

        return image;
    }

    /**
     * Method called by populateTeacher(), used to print the teachers inside the board
     * @param color is the color of the teacher owned by the player
     * @return the modified imageView
     */
    private ImageView generateTeacher(Color color){
        ImageView teacher = new ImageView(color.getTeacherImg());
        teacher.setFitWidth(25);
        teacher.setFitHeight(30);

        return teacher;
    }

    @Override
    public void close() {

    }
}
