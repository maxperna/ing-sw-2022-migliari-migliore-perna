package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.Expert1;
import it.polimi.ingsw.model.experts.Expert3;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Expert1SceneController extends ViewSubject implements GenericSceneController {

    private Map<Integer, IslandNode> islandList;
    private final int cardID;
    private final Expert1 card;
    private final ArrayList<Color> studentsOnCard;
    private Color colorPicked;

    @FXML
    AnchorPane ParentNode;
    @FXML
    ChoiceBox IslandSelector;
    @FXML
    StackPane Island;
    @FXML
    Button chooseIsland;
    @FXML
    HBox StudentSpace;

    public Expert1SceneController( int cardID, Expert1 card) {
        this.islandList = card.getIslandMap();
        this.studentsOnCard = card.getStudentsOnCard();
        this.cardID = cardID;
        this.card = card;
    }

    public void initialize() {

        IslandSelector.setItems(FXCollections.observableArrayList(islandList.keySet()));
        IslandSelector.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceIsland);
        chooseIsland.addEventHandler(MouseEvent.MOUSE_PRESSED,this::playExpert);
        Island.setVisible(false);
        for(Color student : card.getStudentsOnCard()){
            ImageView studToPlace = new ImageView(student.getStudImg());
            studToPlace.setId(student.toString());
            studToPlace.setFitWidth(30);
            studToPlace.setFitHeight(55);
            StudentSpace.getChildren().add(studToPlace);
            studToPlace.addEventHandler(MouseEvent.MOUSE_CLICKED,this::selectStudent);
        }
    }

    public void selectStudent(MouseEvent event){
        Node selectedColor = event.getPickResult().getIntersectedNode();
        event.consume();
        if(selectedColor.getId().equals(Color.RED.toString()))
            colorPicked = Color.RED;
        else if (selectedColor.getId().equals(Color.BLUE.toString()))
            colorPicked = Color.BLUE;

        else if (selectedColor.getId().equals(Color.GREEN.toString()))
            colorPicked = Color.GREEN;

        else if (selectedColor.getId().equals(Color.PINK.toString()))
            colorPicked = Color.PINK;
        else
            colorPicked = Color.YELLOW;
    }

    public void choiceIsland(MouseEvent event){
        renderIsland();
        event.consume();
    }

    private void renderIsland(){
        int ID = (int)IslandSelector.getValue();
        IslandNode nodeToRender = islandList.get(ID);
        Set<Color> colorOnIsland = new HashSet<>(nodeToRender.getStudents());

        for(Color color:colorOnIsland){
            Island.getChildren().get(0).lookup("#"+color.toString()).setVisible(true);
            Label studentCount = (Label) ((AnchorPane) Island.getChildren().get(0).lookup("#"+color.toString())).getChildren().get(0);

            studentCount.setText(nodeToRender.getColorInfluence(color).toString());
        }

    }

    public void playExpert(MouseEvent event){
        new Thread(()->notifyListener(l->l.playExpertCard3(cardID,(int)IslandSelector.getValue(),colorPicked))).start();
        event.consume();
        close();
    }
    @Override
    public void close() {
        ((Stage) ParentNode.getScene().getWindow()).close();
    }
}

