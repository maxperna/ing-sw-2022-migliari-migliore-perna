package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.model.Color.*;

public class PlayerViewController extends ViewSubject implements SceneControllerInt {
    @FXML
    GridPane gameField;
    @FXML
    GridPane diningRoom;
    @FXML
    GridPane entryRoom;
    @FXML
    GridPane towerSpace;
    @FXML
    GridPane professorSpace;

    Map<Color,Node> teacherList;

    private Board currentBoard;
    private boolean studentOnMovement;
    private Node tempNode;


    public PlayerViewController(){
        teacherList = new ConcurrentHashMap<>();
    }

    @FXML
    public void initialize(){
        populateEntry();
        diningRoom.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudent);
    }


    public void moveStudent(MouseEvent event){
        Color colorPicked = null;
        switch (tempNode.getId()){
            case "GREEN":
                colorPicked = GREEN;
                break;
            case "RED":
                colorPicked = RED;
                break;
            case "BLUE":
                colorPicked = BLUE;
                break;
            case "YELLOW":
                colorPicked = YELLOW;
                break;
            case "PINK":
                colorPicked = PINK;
                break;
        }

        Color colorPickedFin = colorPicked;
        diningRoom.add(tempNode,0,colorPickedFin.getBoardIndex());
        notifyListener(l->l.moveStudentToDinner(colorPickedFin));
    }

    public void startMovement(MouseEvent event){
        tempNode = event.getPickResult().getIntersectedNode();
        studentOnMovement = true;
    }

    private void populateEntry(){

        int k=0;
        for(int i=0;i<2;i++){
            for(int j=0;j<currentBoard.getEntryRoom().size()%2+1;j++){
                if(!(j==0 && i==0)) {
                    Color colorToAdd = currentBoard.getEntryRoom().get(k);
                    //Node adding
                    Node student = new ImageView(colorToAdd.getStudImg());
                    student.setId(colorToAdd.toString());
                    student.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startMovement);
                    entryRoom.setAlignment(Pos.CENTER);
                    student.setScaleX(0.7);
                    student.setScaleY(0.7);
                    entryRoom.add(student, i, j);
                    k++;
                }
            }
        }

    }

    private void addTeacher(Color color){

        Node teacher = new ImageView(color.getTeacherImg());
        teacher.setScaleX(0.6);
        teacher.setScaleY(0.6);
        professorSpace.add(teacher,0,color.getBoardIndex());
        teacherList.put(color,teacher);  //adding node and references
    }

    public void removeTeacher(Color color){
        if(teacherList.get(color)!=null)
            professorSpace.getChildren().remove(teacherList.get(color));
    }

    public void setCurrentBoard(Board board){
        this.currentBoard = board;
    }


}
