package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.experts.Expert5;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class Expert5SceneController extends ViewSubject implements GenericSceneController{

    private Map<Integer, IslandNode> islandList;
    private final int cardID;
    private final Expert5 card;

    @FXML
    AnchorPane ParentNode;
    @FXML
    ChoiceBox IslandSelector;
    @FXML
    StackPane Island;
    @FXML
    Button chooseIsland;

    public Expert5SceneController( int cardID, Expert5 card) {
        this.islandList = card.getIslandList();
        this.cardID = cardID;
        this.card = card;
    }

    public void initialize() {

        IslandSelector.setItems(FXCollections.observableArrayList(islandList.keySet()));
        IslandSelector.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceIsland);
        chooseIsland.addEventHandler(MouseEvent.MOUSE_PRESSED,this::playExpert);
        Island.setVisible(false);
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
        new Thread(()->notifyListener(l->l.playExpertCard2(cardID,(int)IslandSelector.getValue()))).start();
        event.consume();
        close();
    }
    @Override
    public void close() {
        ((Stage) ParentNode.getScene().getWindow()).close();
    }
}
