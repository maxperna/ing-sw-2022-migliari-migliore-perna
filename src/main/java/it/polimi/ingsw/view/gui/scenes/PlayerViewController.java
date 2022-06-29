package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;

import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.model.Color.*;

public class PlayerViewController extends ViewSubject implements GenericSceneController {
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
    @FXML
    ChoiceBox<String> actionButton;

    private final ImageView MN;

    private final Map<Color,Node> teacherList;
    private final Map<Integer,GridPane> islandList;
    private final Map<Integer,GridPane> cloudList;
    private final Map<Color,ArrayList<Node>> studentsOnDining;
    private final Map<Integer,Map<String,AnchorPane>> islandConfig;

    private boolean studentOnMovement;
    private boolean MNOnMovement;
    private int previousMNPosition;  //position of MN on island before movement
    private Node tempNode;



    public PlayerViewController(){
        teacherList = new ConcurrentHashMap<>();
        islandList = new ConcurrentHashMap<>();
        cloudList = new ConcurrentHashMap<>();
        islandConfig = new ConcurrentHashMap<>();
        studentsOnDining = new ConcurrentHashMap<>();
        MN = new ImageView("images/Scontornate/mother_nature_pawn.png");
        MN.setFitWidth(52);
        MN.setFitHeight(71);
        MN.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startMNMovement);
        MN.setVisible(false);
        //MN.setDisable(true);

    }

    @FXML
    public void initialize(){
        generateGameField();
        diningRoom.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudentBoard);
        actionButton.getItems().removeAll();
        actionButton.setItems(FXCollections.observableArrayList("Play Card", "Show Boards"));
        Platform.runLater(() -> {
            SkinBase<ChoiceBox<String>> skin = (SkinBase<ChoiceBox<String>>) actionButton.getSkin();

            for (Node child : skin.getChildren()) {
                if (child instanceof Label) {
                    Label label = (Label) child;
                    if (label.getText().isEmpty()) {
                        label.setText("Select Move");
                    }
                    return;
                }
            }
        });
        actionButton.setDisable(true);
    }

    public void setPreparationPhaseChoiceBox(){

        actionButton.setDisable(false);

        actionButton.setOnAction(actionEvent -> {
            String selection = (String)actionButton.getSelectionModel().getSelectedItem();
            if (selection.equals("Play Card")) {
                new Thread(() -> notifyListener(l -> l.chooseAction(1))).start();
                actionButton.getItems().remove(0);
            } else
                new Thread(() -> notifyListener(l -> l.chooseAction(2))).start();

        });

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
            new Thread(()->notifyListener(l->l.moveStudentToDinner(colorPicked))).start();
        }
    }

    /**Method to move a student or mn on a clicked island*/
    public void pickSelectedIsland(MouseEvent event){
        try {
            Node clickedIsland = event.getPickResult().getIntersectedNode();
            int ID = Integer.parseInt(clickedIsland.getId());

            if (studentOnMovement) {

                Color colorPicked = fromStringToColor(tempNode.getId());
                entryRoom.getChildren().remove(tempNode);

                AnchorPane referringPane = islandConfig.get(ID).get(colorPicked.toString());
                Label counter = (Label) referringPane.getChildren().get(0);
                Integer tempValue = Integer.parseInt(counter.getText());
                tempValue++;
                counter.setText((tempValue).toString());
                referringPane.setVisible(true);


                changeStudMovState();
                event.consume();

                new Thread(()->notifyListener(l->l.moveStudentToIsland(colorPicked,ID))).start();
            } else if (MNOnMovement) {
                int numOfSteps = ID - previousMNPosition;

                if(numOfSteps>0){
                    changeMNonMovState();
                    event.consume();
                    new Thread(()->notifyListener(l->l.moveMotherNature(numOfSteps))).start();
                }
            }
        }catch (NumberFormatException e){
            //Clicking on another node I can remake my selection (both on MN and students)
            tempNode = null;
            if(studentOnMovement)
                changeStudMovState();
            if(MNOnMovement)
                changeMNonMovState();

        }
    }

    /**Method to signal that a student has been picked*/
    public void startMovement(MouseEvent event){
        tempNode = event.getPickResult().getIntersectedNode();
        changeStudMovState();
        event.consume();
    }

    public void startMNMovement(MouseEvent event){
        changeMNonMovState();
        event.consume();
    }

    /**Method to set the entry room of the board*/
    private void populateEntry(ArrayList<Color> entry) {
        int k =0;
        int bound = entry.size();
        entryRoom.getChildren().clear();
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
                    entryRoom.add(student, i, j);
                    k++;
                }
            }
        }
    }

    public void updateGameField(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Board board, ArrayList<ExpertCard> experts, int numOfCoins){
        populateBoard(board);
        populateIslands(gameFieldMap);
        populateCloud(chargedClouds);
    }
    public void populateBoard(Board board){
        populateEntry(board.getEntryRoom());
        createTowers(board.getTowerColor(),board.getNumOfTowers());
        setTeachers(board.getTeachers());
    }


    private void createTowers(TowerColor tower,int numOfTowers){
        String towerImage = tower.getTowerImg();
        for(int i=0;i<2;i++){
            for(int j=0;j<numOfTowers/2;j++){
                ImageView towerIMGV = new ImageView(towerImage);
                towerIMGV.setFitWidth(29);
                towerIMGV.setFitHeight(69);
                towerSpace.add(towerIMGV,i,j);
            }
        }
    }
    private void setTeachers(Map<Color,Boolean> teachers){

        for(Color color:teachers.keySet() ) {
            if(teachers.get(color)) {
                ImageView teacherIMG = generateTeacher(color);
                professorSpace.add(teacherIMG, 0, color.getBoardIndex());
                teacherList.put(color, teacherIMG);  //adding node and references
            }
            else{
                try{
                    professorSpace.getChildren().remove(teacherList.get(color));
                    teacherList.remove(color);
                }catch (Exception e){
                    assert true;
                }
            }
        }
    }



    /**Method to populate cloud with student
     * @param clouds array list of clouds*/
    public void populateCloud(ArrayList<CloudTile> clouds){
        for(CloudTile cloud:clouds){
            GridPane cloudStruct = cloudList.get(cloud.getTileID());
            cloudStruct.getChildren().clear();
            int j;
            for(int i=0;i<cloud.getStudents().size();i++){
                j = i%2;
                ImageView student = studentGenerator(cloud.getStudents().get(i));
                cloudStruct.add(student,j,i);
            }
        }

    }

    /**Method which modify the game field due to world update
     * @param islandsMap list of the islands sent by model*/
    public void populateIslands(Map<Integer, IslandNode> islandsMap){
        //Find nodes that no longer exist in model and delete it
        Set<Integer> nodeDiff =  new HashSet<>(islandList.keySet());
        nodeDiff.removeAll(islandsMap.keySet());

        for (int idToDel:nodeDiff){
            gameField.getChildren().remove(islandList.get(idToDel));
        }
        for(int ID:islandsMap.keySet()){
            IslandNode island = islandsMap.get(ID);
            Set<Color> colorOnIsland = new HashSet<>(islandsMap.get(ID).getStudents());
            if(island.checkMotherNature()) {
                previousMNPosition = ID;
                try {
                    islandList.get(ID).add(MN,0,0);
                    MN.setVisible(true);
                }catch (IllegalArgumentException e){
                    assert true;
                }

            }
            if(island.isStopped()){
                ImageView denyTile = new ImageView("images/Scontornate/deny_tile.png");
                denyTile.setId("deny");
                denyTile.setFitHeight(43);
                denyTile.setFitWidth(43);
                denyTile.setDisable(true);
                islandList.get(ID).add(denyTile,2,2);
            }
            if(islandList.get(ID).lookup("#deny")!= null){
                if(!island.isStopped())
                    islandList.get(ID).getChildren().remove(islandList.get(ID).lookup("#deny"));
            }

            //Color counting
            if(!island.getTowerColor().equals(TowerColor.EMPTY)){
                TowerColor tColor = island.getTowerColor();
                //Sostituisco la torre
                if(islandConfig.get(ID).get("TowerSpace").lookup("#"+tColor.toString()) != null)
                    islandConfig.get(ID).remove(islandConfig.get(ID).get("TowerSpace").lookup("#"+tColor.toString()));

                Label towerLabel = (Label) islandConfig.get(ID).get("TowerSpace").getChildren().get(0);
                towerLabel.setText(island.getNumberOfTowers().toString());
                ImageView towerToAdd = new ImageView(island.getTowerColor().getTowerImg());
                towerToAdd.setId(island.getTowerColor().toString());
                towerToAdd.setFitWidth(41);
                towerToAdd.setFitHeight(63);
                towerToAdd.setDisable(true);

            }
            for(Color color:colorOnIsland){
                islandConfig.get(ID).get(color.toString()).setVisible(true);
                Label studentCount = (Label) islandConfig.get(ID).get(color.toString()).getChildren().get(0);

                studentCount.setText(island.getColorInfluence(color).toString());
            }
            //Settare tutti i parametri, compreso flag is stopped;
        }
    }

    /**Method to activate deactivate gameField(set disabled if enabled and vice-versa*/
    public void changeGameFieldStatus(){
        gameField.setDisable(!gameField.isDisabled());
    }

    /**Method to activate or deactivate cloud selection*/
    public void changeCloudStatus(){
        for(int ID : cloudList.keySet())
            cloudList.get(ID).getParent().setDisable(!cloudList.get(ID).isDisabled());
    }
    /**Method to choice cloud from whom take the students*/
    public void choiceCloud(MouseEvent event){
        Node clickedCloud = event.getPickResult().getIntersectedNode();
        int ID = Integer.parseInt(clickedCloud.getId());
        //clickedCloud.setDisable(true);
        new Thread(()->notifyListener(l->l.chooseCloudTile(ID))).start();
    }

    /**Method to activate MN when it's time to move it*/
    public void activateMN(){
        MN.setDisable(false);
    }

    /**Method to generate the game field assigning island the correct id*/
    public void generateGameField() {
        Integer cloudIndex = 0;
        for (Node gameFieldEl : gameField.getChildren()) {
            //Setting grid as id in order to access directly to its component
            if (gameFieldEl.getId() != null){
                GridPane islandStruct = (GridPane) ((StackPane) gameFieldEl).getChildren().get(0);
                islandStruct.addEventHandler(MouseEvent.MOUSE_CLICKED, this::pickSelectedIsland);
                islandStruct.setId(gameFieldEl.getId());
                islandList.put(Integer.parseInt(gameFieldEl.getId()), islandStruct);
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

                islandConfig.put(Integer.parseInt(gameFieldEl.getId()), tempConfig);
            }
            else {

                GridPane cloudStruct = (GridPane) ((StackPane) gameFieldEl).getChildren().get(0);
                cloudStruct.setId(cloudIndex.toString());  //CLOUD#
                //cloudStruct.getParent().setDisable(true);
                cloudStruct.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceCloud);
                cloudList.put(cloudIndex,cloudStruct);
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


    private void changeStudMovState(){
        studentOnMovement = !studentOnMovement;
    }

    public void changeMNonMovState(){
        MNOnMovement = !MNOnMovement;
    }



}