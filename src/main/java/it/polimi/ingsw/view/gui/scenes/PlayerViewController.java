package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Color;

import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static it.polimi.ingsw.model.Color.*;
import static java.lang.Math.abs;

/**
 * Class that manages all the interactions on the gameField
 */
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
    VBox buttonVBox;
    @FXML
    Button playCardButton;
    @FXML
    Button showBoardsButton;
    @FXML
    Button playExpertButton;
    @FXML
    Label currentPlayerLabel;
    @FXML
    Label numOfCoinLabel;

    private final ImageView MN;
    private final Map<Color,Node> teacherList;
    private  Map<Integer,GridPane> islandList;
    private final Map<Integer,GridPane> cloudList;
    private  Map<Integer,Map<String,AnchorPane>> islandConfig;
    private boolean expertMode;
    private boolean studentOnMovement;
    private boolean MNOnMovement;
    private int previousMNPosition;  //position of MN on island before movement
    private Node tempNode;
    private ArrayList<ExpertCard> expertsCard;
    private int numOfCoins;
    private ArrayList<Node> islandNodes;


    /**
     * Default constructor
     */
    public PlayerViewController(){
        teacherList = new ConcurrentHashMap<>();
        islandList = new ConcurrentHashMap<>();
        cloudList = new ConcurrentHashMap<>();
        islandConfig = new ConcurrentHashMap<>();
        islandNodes = new ArrayList<>();
        MN = new ImageView("images/Scontornate/mother_nature_pawn.png");
        MN.setFitWidth(52);
        MN.setFitHeight(71);
        MN.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startMNMovement);
        MN.setVisible(false);
    }

    /**
     * Method used to initialize the panel by setting all the events
     */
    @FXML
    public void initialize(){
        MN.setDisable(true);
        generateGameField();
        entryRoom.setDisable(true);
        switchCloudStatus();
        playExpertButton.setDisable(true);

        diningRoom.addEventHandler(MouseEvent.MOUSE_CLICKED,this::moveStudentBoard);
        for(Node node : buttonVBox.getChildren())
            node.setDisable(true);

        playCardButton.setOnAction(actionEvent -> {
            new Thread(() -> notifyListener(l -> l.chooseAction(1))).start();
            playCardButton.setDisable(true);
            showBoardsButton.setDisable(true);
        });

        for(int i = 1; i <= islandList.size(); i++) {
            Node island = gameField.lookup("#"+i);
            islandNodes.add(island);
        }
        showBoardsButton.setOnAction(actionEvent -> new Thread(() -> notifyListener(l -> l.chooseAction(2))).start());

        playExpertButton.setOnAction(actionEvent-> new Thread(()->notifyListener(l->l.guiExpertShow(expertsCard,false,numOfCoins))).start());

    }

    /**
     * Method used to manage the available buttons during preparation phase
     */
    public void setPreparationPhaseChoiceBox(){

        playCardButton.setDisable(false);
        showBoardsButton.setDisable(false);

        if(expertMode)
            playExpertButton.setDisable(false);

    }


    /**Method to move students from entry room to dining room*/
    public void moveStudentBoard(MouseEvent event){
        if(studentOnMovement) {
            Color colorPicked = fromStringToColor(tempNode.getId());
            tempNode.setDisable(true);
            tempNode = null;
            event.consume();
            entryRoom.getChildren().remove(tempNode);
            entryRoom.setDisable(true);
            changeStudMovState();
            new Thread(()->notifyListener(l->l.moveStudentToDinner(colorPicked))).start();

        }
    }

    /**Method to move a student or mn on a clicked island*/
    public void pickSelectedIsland(MouseEvent event){
        try {
            Node clickedIsland = event.getPickResult().getIntersectedNode();
            int ID = Integer.parseInt(clickedIsland.getId());
            Logger.getLogger("PWC").info("island id "+ID);
            if (studentOnMovement) {

                Logger.getLogger("PWC").info("PWC stud moving");
                Color colorPicked = fromStringToColor(tempNode.getId());
                entryRoom.getChildren().remove(tempNode);

                AnchorPane referringPane = islandConfig.get(ID).get(colorPicked.toString());
                Label counter = (Label) referringPane.getChildren().get(0);
                Integer tempValue = Integer.parseInt(counter.getText());
                tempValue++;
                counter.setText((tempValue).toString());
                referringPane.setVisible(true);

                event.consume();
                changeStudMovState();
                entryRoom.setDisable(true);
                new Thread(()->notifyListener(l->l.moveStudentToIsland(colorPicked,ID))).start();


            } else if (MNOnMovement) {
                int numOfSteps =ID - previousMNPosition;   //avoiding problem passing from 12 to 1
                if(numOfSteps<0)
                    numOfSteps = islandList.size()%(abs(numOfSteps));

                Logger.getLogger("PWC").info("MN moving");

                changeMNonMovState();
                event.consume();
                MN.setOpacity(1);
                MN.setDisable(true);
                int finalNumOfSteps = numOfSteps;
                new Thread(()->notifyListener(l->l.moveMotherNature(finalNumOfSteps))).start();
                Logger.getLogger("PWC").info("MN moved with steps "+finalNumOfSteps);


            }
        }catch (NumberFormatException e){
            //Clicking on another node I can remake my selection (both on MN and students)
            if(tempNode!=null)
                tempNode.setOpacity(1);
            tempNode = null;
            if(studentOnMovement)
                changeStudMovState();
            if(MNOnMovement) {
                changeMNonMovState();
                Logger.getLogger("PWC").info("MN deselected");
            }
        }
    }

    /**Method to signal that a student has been picked*/
    public void startStudentMovement(MouseEvent event){
        changeStudMovState();
        tempNode = event.getPickResult().getIntersectedNode();
        tempNode.setOpacity(0.8);
        event.consume();
    }

    /**
     * Method used to notify mother nature movement
     * @param event
     */
    public void startMNMovement(MouseEvent event){
        MN.setOpacity(0.8);
        changeMNonMovState();
        event.consume();
    }


    /**Method to update the game info due to a world change message, updating gamefield and boards*/
    public void updateGameField(Map<Integer, IslandNode> gameFieldMap, ArrayList<CloudTile> chargedClouds, Board board, String currentPlayer,ArrayList<ExpertCard> experts, int numOfCoins){
        currentPlayerLabel.setText(currentPlayer);
        if(experts.size()>0) {
            numOfCoinLabel.setText(Integer.toString(numOfCoins));
            this.numOfCoins = numOfCoins;
            expertMode = true;
        }
        expertsCard = experts;
        populateBoard(board);
        populateIslands(gameFieldMap);
        populateCloud(chargedClouds);
    }

    /**
     * Method used to fill the board with the students that are set based on the model board
     * @param board is the player's board
     */
    public void populateBoard(Board board){
        populateEntry(board.getEntryRoom());
        popoulateDining(board.getDiningRoom());
        createTowers(board.getTowerColor(),board.getNumOfTowers());
        setTeachers(board.getTeachers());
    }

    /**
     * Method called by populateBoard() to set the students inside the entry room
     * @param entry is an arrayList containing all the students that will be placed on the entry room
     */
    private void populateEntry(ArrayList<Color> entry) {
        int placedElements = 0;
        int bound = (entry.size() / 2)+1;
        entryRoom.getChildren().clear();
        for (int i = 0; i < bound; i++) {
            int j = 0;
            while (j < 2 && placedElements < entry.size()) {
                if (!(i == 0 && j == 0)) {
                    Color colorToAdd = entry.get(placedElements);
                    ImageView student = studentGenerator(colorToAdd);
                    entryRoom.add(student, j, i);
                    placedElements++;
                }
                j++;
            }
        }
    }

    /**
     * Method called by populateBoard() to set the students inside the dining room
     * @param dining is a map containing all the students that are placed inside the dining room
     */
    private void popoulateDining(Map<Color,Integer> dining){
        for(Color color : dining.keySet()) {
            for(int i = 0; i < dining.get(color); i ++) {
                ImageView student = studentGenerator(color);
                student.setDisable(true);
                diningRoom.add(student, i, color.getBoardIndex());
            }
        }
    }

    /**
     * Method called by populateBoard() to set all the available towers
     * @param tower is the color associated to this board's towers
     * @param numOfTowers is the number of available towers
     * */
    private void createTowers(TowerColor tower,int numOfTowers){
        String towerImage = tower.getTowerImg();
        towerSpace.getChildren().clear();
        int placedElement = 0;
        int bound = numOfTowers/2;
        if(bound%2==1)
            bound = bound+1;

        for(int i=0;i<bound;i++){
            int j = 0;
            while (j<2 && placedElement<numOfTowers) {
                ImageView towerIMGV = new ImageView(towerImage);
                towerIMGV.setFitWidth(29);
                towerIMGV.setFitHeight(69);
                towerSpace.add(towerIMGV, j, i);
                j++;
                placedElement++;
            }
        }
    }

    /**
     * Method called by populateBoard() to set all the owned teachers
     * @param teachers is a map containing information about all the owned teachers
     */
    private void setTeachers(Map<Color,Boolean> teachers){

        professorSpace.getChildren().clear();
        for(Color color:teachers.keySet() ) {
            if(teachers.get(color)) {
                ImageView teacherIMG = generateTeacher(color);
                professorSpace.add(teacherIMG, 0, color.getBoardIndex());
                teacherList.put(color, teacherIMG);  //adding node and references
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
                student.setDisable(true);
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

        if(nodeDiff.size()>0){
            refactorGameField(nodeDiff);
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

            Label towerLabel = (Label) islandConfig.get(ID).get("TowerSpace").getChildren().get(0);
            towerLabel.setText("0");
            try {
                islandConfig.get(ID).get("TowerSpace").getChildren().remove(1);
            }catch (IndexOutOfBoundsException e){}

            //Color counting
            if(!island.getTowerColor().equals(TowerColor.EMPTY)){
                TowerColor tColor = island.getTowerColor();
                //Sostituisco la torre
                if(islandConfig.get(ID).get("TowerSpace").lookup("#"+tColor.toString()) != null)
                    islandConfig.get(ID).remove(islandConfig.get(ID).get("TowerSpace").lookup("#"+tColor));


                islandConfig.get(ID).get("TowerSpace").setVisible(true);
                towerLabel.setText(island.getNumberOfTowers().toString());
                towerLabel.setVisible(true);
                ImageView towerToAdd = new ImageView(island.getTowerColor().getTowerImg());
                towerToAdd.setId(island.getTowerColor().toString());
                towerToAdd.setFitWidth(41);
                towerToAdd.setFitHeight(63);
                islandConfig.get(ID).get("TowerSpace").getChildren().add(towerToAdd);


            }
            for(Color color:colorOnIsland){
                islandConfig.get(ID).get(color.toString()).setVisible(true);
                Label studentCount = (Label) islandConfig.get(ID).get(color.toString()).getChildren().get(0);

                studentCount.setText(island.getColorInfluence(color).toString());
            }
            //Settare tutti i parametri, compreso flag is stopped;
        }
    }


    /**Method to refactor the gamefield after a merge, reassigning correct id to nodes
     * @param deletedNode node that no longer exists*/
    private void refactorGameField(Set<Integer> deletedNode){
        for(Integer nodeId:deletedNode){
            Node nodeToDelete = islandList.get(nodeId).getParent();     //anchor pane to delete
            gameField.getChildren().remove(nodeToDelete);
            islandList.remove(nodeId);
            islandConfig.remove(nodeId);

            //Temp map to reassign value in maps
            Map<Integer,GridPane> tempIslandMap = new ConcurrentHashMap<>();
            Map<Integer,Map<String,AnchorPane>>  tempIslandMapConfig = new ConcurrentHashMap<>();
            Integer newIndex = 1;

            for(Integer islandID:islandList.keySet()){
                islandList.get(islandID).getParent().setId(newIndex.toString());    //rearraging island id
                //Rearraging island map
                tempIslandMap.put(newIndex,islandList.get(islandID));
                tempIslandMapConfig.put(newIndex,islandConfig.get(islandID));
                newIndex++;
            }

            islandList = tempIslandMap;
            islandConfig = tempIslandMapConfig;

        }

    }
    /**Method to activate or deactivate cloud selection*/
    public void switchCloudStatus(){
        for(int ID : cloudList.keySet())
            cloudList.get(ID).getParent().setDisable(!cloudList.get(ID).isDisabled());
    }
    /**Method to choice cloud from whom take the students*/
    public void choiceCloud(MouseEvent event){
        Node clickedCloud = event.getPickResult().getIntersectedNode();
        int ID = Integer.parseInt(clickedCloud.getId());
        switchCloudStatus();
        new Thread(()->notifyListener(l->l.chooseCloudTile(ID))).start();
    }

    /**Method to activate MN when it's time to move it*/
    public void enableMNMovement(){
        MN.setDisable(false);
    }
    /**Switch the activation of the selection of the students on the board entry room*/
    public void switchStudentMovementStatus(boolean studentMove){
        entryRoom.setDisable(studentMove);
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
                        if (counter.getText().equals("0")) {
                            configuration.setVisible(false); //initially no student is on island
                            configuration.setDisable(true);
                        }
                    } catch (ClassCastException | IndexOutOfBoundsException e) {
                        configuration.setVisible(false); //initially no student is on island
                        configuration.setDisable(true);
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

    /**
     * Method called by populateDiningRoom() and populateEntryRoom(), used to print the students inside the board
     * @param color is the color of the student that will be printed
     * @return the modified imageView
     */
    private ImageView studentGenerator(Color color){
        ImageView image = new ImageView(color.getStudImg());
        image.setFitWidth(36);
        image.setFitHeight(40);
        image.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startStudentMovement);
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
        teacher.setFitWidth(36);
        teacher.setFitHeight(40);
        teacher.setDisable(true);

        return teacher;
    }


    private void changeStudMovState(){
        studentOnMovement = !studentOnMovement;
    }

    public void changeMNonMovState(){
        MNOnMovement = !MNOnMovement;
    }

    public ArrayList<Node> getIslandNodes() {
        return new ArrayList<>(islandNodes);
    }

    @Override
    public void close() {

    }
}