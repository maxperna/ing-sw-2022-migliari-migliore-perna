package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    public BoardInfoSceneController(Map<String, Board> boardMap) {
        this.boardMap = boardMap;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<Pair<String, Board>> pairBoardList = new ArrayList<>();
        Map<Color, Node> teacherList;
        Map<Color, ArrayList<Node>> studentsOnDining;
        Map<Color, ArrayList<Node>> studentsOnEntrance;

        for (String nickName : boardMap.keySet())
            pairBoardList.add(new Pair<>(nickName, boardMap.get(nickName)));

        if(pairBoardList.size() == 2){
            StackPane stackPane1 = new StackPane();
            StackPane stackPane2 = new StackPane();

            anchorPane.getChildren().add(stackPane1);
            AnchorPane.setTopAnchor(stackPane1, 2.0);
            AnchorPane.setLeftAnchor(stackPane1, 2.0);
            AnchorPane.setRightAnchor(stackPane1, 2.0);
            AnchorPane.setBottomAnchor(stackPane1, 295.0);
            ImageView boardImage = new ImageView(new Image("images/Plancia/Plancia_DEF.png", stackPane1.getWidth(), stackPane1.getHeight(), true, false));
            stackPane1.getChildren().add(boardImage);



            anchorPane.getChildren().add(stackPane2);
            AnchorPane.setTopAnchor(stackPane2, 295.0);
            AnchorPane.setLeftAnchor(stackPane2, 2.0);
            AnchorPane.setRightAnchor(stackPane2, 2.0);
            AnchorPane.setBottomAnchor(stackPane2, 2.0);
            createBoard(stackPane2);

        }

        if(pairBoardList.size() == 3) {
            StackPane stackPane1 = new StackPane();
            StackPane stackPane2 = new StackPane();
            StackPane stackPane3 = new StackPane();

            anchorPane.getChildren().add(stackPane1);
            AnchorPane.setTopAnchor(stackPane1, 0.0);
            AnchorPane.setLeftAnchor(stackPane1, 0.0);
            AnchorPane.setRightAnchor(stackPane1, 399.9);
            AnchorPane.setBottomAnchor(stackPane1, 299.9);

            anchorPane.getChildren().add(stackPane2);
            AnchorPane.setTopAnchor(stackPane2, 299.9);
            AnchorPane.setLeftAnchor(stackPane2, 0.0);
            AnchorPane.setRightAnchor(stackPane2, 399.9);
            AnchorPane.setBottomAnchor(stackPane2, 0.0);

            anchorPane.getChildren().add(stackPane3);
            AnchorPane.setTopAnchor(stackPane3, 0.0);
            AnchorPane.setLeftAnchor(stackPane3, 399.9);
            AnchorPane.setRightAnchor(stackPane3, 0.0);
            AnchorPane.setBottomAnchor(stackPane3, 0.0);
        }
    }

//    public void populateBoard(Board board) {
//        populateEntry(board.getEntryRoom());
//        createTowers(board.getTowerColor(), board.getNumOfTowers());
//        setTeachers(board.getTeachers());
//    }
//
//    private void populateEntry(ArrayList<Color> entry) {
//        int k = 0;
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < entry.size() / 2 + 1; j++) {
//                if (!(i == 0 && j == 0)) {
//                    Color colorToAdd = entry.get(k);
//                    ImageView student = studentGenerator(colorToAdd);
//                    entryRoom.add(student, i, j);
//                    k++;
//                }
//            }
//        }
//    }
//
//    private void createTowers(TowerColor tower, int numOfTowers) {
//        String towerImage = tower.getTowerImg();
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < numOfTowers / 2; j++) {
//                ImageView towerIMGV = new ImageView(towerImage);
//                towerIMGV.setFitWidth(29);
//                towerIMGV.setFitHeight(69);
//                towerSpace.add(towerIMGV, i, j);
//            }
//        }
//    }
//
//    private void setTeachers(Map<Color, Boolean> teachers) {
//
//        for (Color color : teachers.keySet()) {
//            if (teachers.get(color)) {
//                ImageView teacherIMG = generateTeacher(color);
//                professorSpace.add(teacherIMG, 0, color.getBoardIndex());
//                teacherList.put(color, teacherIMG);  //adding node and references
//            } else {
//                try {
//                    professorSpace.getChildren().remove(teacherList.get(color));
//                    teacherList.remove(color);
//                } catch (Exception e) {
//                    assert true;
//                }
//            }
//        }
//    }

    private ImageView studentGenerator(Color color) {
        ImageView image = new ImageView(color.getStudImg());
        image.setFitWidth(36);
        image.setFitHeight(40);
        image.setId(color.toString());

        return image;
    }

    private void createBoard(Pane pane) {
        ImageView boardImage = new ImageView(new Image("images/Plancia/Plancia_DEF.png", pane.getWidth(), pane.getHeight(), true, false));
        pane.getChildren().add(boardImage);
    }
}
