package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.jetbrains.annotations.TestOnly;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

    public BoardInfoSceneController(Map<String, Board> boardMap) {
        this.boardMap = boardMap;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Pair<String, Board>> pairList = new ArrayList<>();

        for (String nickName : boardMap.keySet())
            pairList.add(new Pair<>(nickName, boardMap.get(nickName)));

        if(boardMap.size() == 1){
            container.lookup("#board2").setVisible(false);
            container.lookup("#board3").setVisible(false);

            AnchorPane node = (AnchorPane) container.lookup("#board1");
            populateBoard(pairList.get(0).getValue(), node);
            label00.setText(pairList.get(0).getKey());
        }
    }

    public void populateBoard(Board board, AnchorPane pane){

        populateEntry(board.getEntryRoom(), (GridPane) pane.lookup("#entryRoom"));
        createTowers(board.getTowerColor(),board.getNumOfTowers(), (GridPane) pane.lookup("#towerSpace"));
        setTeachers(board.getTeachers(), (GridPane) pane.lookup("#professorSpace"));
    }

    private void populateEntry(ArrayList<Color> entry, GridPane room) {
        int k =0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < entry.size()/2+1; j++) {
                if(!(i==0 && j==0)) {
                    Color colorToAdd = entry.get(k);
                    ImageView student = studentGenerator(colorToAdd);
                    room.add(student, i, j);
                    k++;
                }
            }
        }
    }

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

    private void setTeachers(Map<Color,Boolean> teachers, GridPane professorSpace){

        for(Color color:teachers.keySet() ) {
            if(teachers.get(color)) {
                ImageView teacherIMG = generateTeacher(color);
                professorSpace.add(teacherIMG, 0, color.getBoardIndex());
            }
        }
    }

    private ImageView studentGenerator(Color color){
        ImageView image = new ImageView(color.getStudImg());
        image.setFitWidth(25);
        image.setFitHeight(25);
        image.setId(color.toString());

        return image;
    }

    private ImageView generateTeacher(Color color){
        ImageView teacher = new ImageView(color.getTeacherImg());
        teacher.setFitWidth(25);
        teacher.setFitHeight(30);

        return teacher;
    }
}
