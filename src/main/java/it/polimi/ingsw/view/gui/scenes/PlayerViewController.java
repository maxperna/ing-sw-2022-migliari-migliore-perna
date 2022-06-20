package it.polimi.ingsw.view.gui.scenes;

import com.google.gson.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.gameField.IslandList;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
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
    Map<Integer, int[]> islandList;
    Map<Integer,int[]> cloudList;

    private Board currentBoard;
    private boolean studentOnMovement;
    private Node tempNode;


    public PlayerViewController(){
        teacherList = new ConcurrentHashMap<>();
    }

    @FXML
    public void initialize(){
        populateEntry();
        generateGameField();
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
        new Thread(()->notifyListener(l->l.moveStudentToDinner(colorPickedFin)));
    }

    public void moveStudentIsland(MouseEvent event){
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

        Node clickedIsland = event.getPickResult().getIntersectedNode();
        int ID = Integer.parseInt(clickedIsland.getId());
        AnchorPane ap = (AnchorPane) clickedIsland;
        ap.getChildren().add(tempNode);
        tempNode.setLayoutX(event.getSceneX());
        tempNode.setLayoutX(event.getSceneY());

        //new Thread(()->notifyListener(l->l.moveStudentToIsland(colorPickedFin,ID)));
    }

    public void startMovement(MouseEvent event){
        tempNode = event.getPickResult().getIntersectedNode();
        studentOnMovement = true;
    }

    private void populateEntry(){

        int k=0;
        for(int i=0;i<2;i++){
            for(int j=0;j<4/*currentBoard.getEntryRoom().size()%2+1*/;j++){
                if(!(j==0 && i==0)) {
                    Color colorToAdd = PINK;//currentBoard.getEntryRoom().get(k);
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

    public void generateGameField(){
        try {
            islandList = new ConcurrentHashMap<>();
            FileReader layoutGameField = new FileReader("src/main/Assets/gameFieldPosition.json");
            JsonObject layoutObj = JsonParser.parseReader(layoutGameField).getAsJsonObject();
            JsonArray layoutInfoIsland = layoutObj.get("islandPosition").getAsJsonArray();
            Integer i = 1;
            for(JsonElement islandInfo : layoutInfoIsland){
                JsonObject islandInfoOBJ = islandInfo.getAsJsonObject().get(i.toString()).getAsJsonObject();
                Integer randomIndex = new Random().nextInt(3)+1;


                Node island = new ImageView("images/Scontornate/isola"+randomIndex+".png");
                island.setScaleX(0.7);
                island.setScaleY(0.7);
                island.setId(i.toString());
                int coordinates[] = {islandInfoOBJ.get("row").getAsInt(),islandInfoOBJ.get("col").getAsInt()};

                gameField.add(island,coordinates[0],coordinates[1]);
                gameField.setAlignment(Pos.CENTER_LEFT);

                Node anchorPane = new AnchorPane();
                anchorPane.setId(i.toString());
                anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudentIsland);
                gameField.add(anchorPane,coordinates[0],coordinates[1]);

                islandList.put(i,coordinates);
                i++;
            }

            cloudList = new ConcurrentHashMap<>();
            JsonArray layoutInfoCloud = layoutObj.get("cloud").getAsJsonArray();
            i = 0;
            for (JsonElement cloudInfo: layoutInfoCloud){
                JsonObject cloudInfoObj = cloudInfo.getAsJsonObject().get(i.toString()).getAsJsonObject();

                Node cloud = new ImageView("images/Scontornate/cloud.png");
                cloud.setScaleX(0.7);
                cloud.setScaleX(0.7);
                cloud.setId(i.toString());
                int coordinates[] = {cloudInfoObj.get("row").getAsInt(),cloudInfoObj.get("col").getAsInt()};

                gameField.add(cloud,coordinates[0],coordinates[1]);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
