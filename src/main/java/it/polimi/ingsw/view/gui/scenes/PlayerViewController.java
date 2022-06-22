package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.model.Color.*;

public class PlayerViewController extends ViewSubject implements SceneControllerInt {
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
    Map<Integer,GridPane> islandList;
    Map<Integer,GridPane> cloudList;
    Map<Color,ArrayList<Node>> studentsOnDining;
    Map<Integer,Map<String,AnchorPane>> islandConfig;

    private boolean studentOnMovement;
    private Node tempNode;



    public PlayerViewController(){
        teacherList = new ConcurrentHashMap<>();
    }

    @FXML
    public void initialize(){
        studentsOnDining = new ConcurrentHashMap<>();
        populateEntry();
        generateGameField();

        diningRoom.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudentBoard);
    }

    /**Method to move students from entry room to dining room*/
    public void moveStudentBoard(MouseEvent event){
        if(studentOnMovement) {
            Color colorPicked = fromStringToColor(tempNode.getId());
            //Eventually initialize student map
            studentsOnDining.putIfAbsent(colorPicked, new ArrayList<>());

            diningRoom.add(tempNode, studentsOnDining.get(colorPicked).size(), colorPicked.getBoardIndex());
            //Adding student as list of node, could be useful for expert implementation
            ArrayList<Node> currentStud = studentsOnDining.get(colorPicked);
            tempNode.setDisable(true);
            currentStud.add(tempNode);
            studentsOnDining.put(colorPicked, currentStud);
            changeStudMovState();

            event.consume();
            //new Thread(()->notifyListener(l->l.moveStudentToDinner(colorPicked)));
        }
    }

    public void moveStudentIsland(MouseEvent event){
        if(studentOnMovement ) {
            try {
                Color colorPicked = fromStringToColor(tempNode.getId());
                Node clickedIsland = event.getPickResult().getIntersectedNode();
                Integer ID = Integer.parseInt(clickedIsland.getId());
                AnchorPane referringPane = islandConfig.get(ID).get(colorPicked.toString());
                Label counter = (Label) referringPane.getChildren().get(0);
                Integer tempValue = Integer.parseInt(counter.getText());
                tempValue++;
                counter.setText((tempValue).toString());
                referringPane.setVisible(true);
                entryRoom.getChildren().remove(tempNode);

                changeStudMovState();
                event.consume();


//                new Thread(()->notifyListener(l->l.moveStudentToIsland(colorPicked,ID)));
            }catch (NumberFormatException e){
                //Clicking on another node I can remake my selection
                tempNode = null;
                changeStudMovState();
            }
        }
    }

    /**Method to signal that a student has been picked*/
    public void startMovement(MouseEvent event){
        tempNode = event.getPickResult().getIntersectedNode();
        changeStudMovState();
    }

    /**Method to set the entry room of the board*/
    private void populateEntry(){
        Color test = RED;
        int k=0;
        for(int i=0;i<2;i++){
            for(int j=0;j<4/*currentBoard.getEntryRoom().size()%2+1*/;j++){
                if(!(j==0 && i==0)) {
                    //Node adding
                    if(j%2 == 0)
                        test = BLUE;
                    if(j%3==0)
                        test = PINK;
                    ImageView student = studentGenerator(test);
                    GridPane.setHalignment(student,HPos.RIGHT);
                    entryRoom.add(student,i,j);
                    k++;
                }
            }
        }

    }


    private void addTeacher(Color color){

        ImageView teacher = generateTeacher(color);
        professorSpace.add(teacher,0,color.getBoardIndex());
        teacherList.put(color,teacher);  //adding node and references
    }

    public void removeTeacher(Color color){
        if(teacherList.get(color)!=null)
            professorSpace.getChildren().remove(teacherList.get(color));
    }

    public void setCurrentBoard(Board board){

    }

    /**Method to populate cloud with student
     * @param ID  ID of the cloud to fill*/
    public void populateCloud(int ID,ArrayList<Color> students){
        //Each cloud is made of a 2*2 grid pane
    }

    /**Method to choice cloud from whom take the students*/
    public void choiceCloud(MouseEvent event){
        //new Thread(()->notifyListener(l->l.chooseCloudTile(ID))).start();
    }

    /**Method to generate the game field assigning island the correct id*/
    public void generateGameField() {
        islandList = new ConcurrentHashMap<>();
        islandConfig = new ConcurrentHashMap<>();
        cloudList = new ConcurrentHashMap<>();
        Integer index = 1;
        Integer cloudIndex = 13;
        for (Node gameFieldEl : gameField.getChildren()) {
            //Setting grid as id in order to access directly to its component
            if (gameFieldEl.getId() == null) {
                GridPane islandStruct = (GridPane) ((StackPane) gameFieldEl).getChildren().get(0);
                islandStruct.addEventHandler(MouseEvent.MOUSE_CLICKED, this::moveStudentIsland);
                islandStruct.setId(index.toString());
                islandList.put(index, islandStruct);
                //Island structure initialization
                Map<String, AnchorPane> tempConfig = new ConcurrentHashMap<>();
                for (Node configuration : islandStruct.getChildren()) {
                    tempConfig.put(configuration.getId(), (AnchorPane) configuration);
                    try {
                        Label counter = (Label) ((AnchorPane) configuration).getChildren().get(0);
                        if (counter.getText().equals("0"))
                            configuration.setVisible(false); //initially no student is on island
                    } catch (ClassCastException | IndexOutOfBoundsException e) {
                        configuration.setVisible(false); //initially no student is on island
                    }
                }

                islandConfig.put(index, tempConfig);
                index++;
            }
            else {

                GridPane cloudStruct = (GridPane) ((StackPane) gameFieldEl).getChildren().get(0);
                cloudStruct.setId(cloudIndex.toString());

                cloudStruct.setDisable(true);
                cloudList.put(cloudIndex%12,cloudStruct);
                cloudIndex++;
            }
        }
    }

    private Color fromStringToColor(String color){
        switch (color){
            case "GREEN":
                return GREEN;
            case "RED":
                return RED;
            case "BLUE":
                return BLUE;
            case "YELLOW":
                return YELLOW;
            case "PINK":
                return PINK;
            default:
                return null;
        }
    }

    private ImageView studentGenerator(Color color){
        ImageView image = new ImageView(color.getStudImg());
        image.setFitWidth(36);
        image.setFitHeight(40);
        image.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startMovement);
        image.setId(color.toString());

        return image;
    }

    private ImageView generateTeacher(Color color){
        ImageView teacher = new ImageView(color.getTeacherImg());
        teacher.setFitWidth(36);
        teacher.setFitHeight(40);

        return teacher;
    }

    private GridPane generateIslandPane(Integer ID){
        GridPane island = new GridPane();
        island.setPrefWidth(270);
        island.setPrefHeight(230);
        island.getStyleClass().add("island-pane");
        island.setId(ID.toString());

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.CENTER);

        return island;
    }

    private void changeStudMovState(){
        studentOnMovement = !studentOnMovement;
    }

}
