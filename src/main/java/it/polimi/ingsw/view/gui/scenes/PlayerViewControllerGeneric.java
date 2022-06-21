package it.polimi.ingsw.view.gui.scenes;

import com.google.gson.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.model.Color.*;

public class PlayerViewControllerGeneric extends ViewSubject implements GenericSceneController {
    @FXML
    AnchorPane gameField;
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
    Map<Integer,GridPane> cloudList;

    private Board currentBoard;
    private boolean studentOnMovement;
    private Node tempNode;


    public PlayerViewControllerGeneric(){
        teacherList = new ConcurrentHashMap<>();
    }

    @FXML
    public void initialize(){
        populateEntry();
        generateGameField();
        /*ArrayList<Color> prova = new ArrayList<>();
        prova.add(PINK);
        prova.add(BLUE);
        prova.add(RED);*/

        //populateCloud(1,prova);
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
        //int ID = Integer.parseInt(clickedIsland.getId());
        AnchorPane ap = (AnchorPane) clickedIsland;
        ((AnchorPane) clickedIsland).getChildren().add(tempNode);
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
                    ImageView student = new ImageView(colorToAdd.getStudImg());
                    student.setId(colorToAdd.toString());
                    student.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startMovement);
                    student.setFitWidth(36);
                    student.setFitHeight(40);
                    entryRoom.add(student, i, j);
                    GridPane.setHalignment(student,HPos.RIGHT);
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

    /**Method to populate cloud with student
     * @param ID  ID of the cloud to fill*/
    public void populateCloud(int ID,ArrayList<Color> students){
        //Each cloud is made of a 2*2 grid pane
        int numOfStud = students.size();
        for(int i =0;i<2;i++){
            int k =0;
            while(k< numOfStud && k<2){
                ImageView student = new ImageView(students.get(i).getStudImg());
                student.setScaleX(0.5);
                student.setScaleY(0.5);
                cloudList.get(ID).setAlignment(Pos.TOP_CENTER);
                cloudList.get(ID).add(student,i,k);
                GridPane.setFillWidth(student,true);
                k++;
                numOfStud--;
            }
        }
    }

    /**Method to choice cloud from whom take the students*/
    public void choiceCloud(MouseEvent event){
        //new Thread(()->notifyListener(l->l.chooseCloudTile(ID))).start();
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


                AnchorPane islandSupport = new AnchorPane();
                ImageView island = new ImageView("images/Scontornate/isola1.png");
                island.setFitWidth(270);
                island.setFitHeight(230);
                island.setDisable(true);
                //islandSupport.getChildren().add(island);
                Label label = new Label();
                label.setId(PINK.toString());
                islandSupport.getChildren().add(label);
                islandSupport.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudentIsland);
                gameField.getChildren().add(islandSupport);
                AnchorPane.setLeftAnchor(islandSupport,islandInfoOBJ.get("left").getAsDouble());
                AnchorPane.setTopAnchor(islandSupport,islandInfoOBJ.get("up").getAsDouble());




                i++;
            }

            /**cloudList = new ConcurrentHashMap<>();
            JsonArray layoutInfoCloud = layoutObj.get("cloud").getAsJsonArray();
            i = 1;
            for (JsonElement cloudInfo: layoutInfoCloud){
                JsonObject cloudInfoObj = cloudInfo.getAsJsonObject().get(i.toString()).getAsJsonObject();

                int coordinates[] = {cloudInfoObj.get("row").getAsInt(),cloudInfoObj.get("col").getAsInt()};

                GridPane cloudStructure = new GridPane();
                cloudStructure.addEventHandler(MouseEvent.MOUSE_CLICKED,this::choiceCloud);
                gameField.add(cloudStructure,coordinates[0],coordinates[1]);
                cloudList.put(i,cloudStructure);
                i++;
            }**/


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
