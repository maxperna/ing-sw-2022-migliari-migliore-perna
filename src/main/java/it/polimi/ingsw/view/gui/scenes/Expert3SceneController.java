package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.Expert3;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Expert3SceneController extends ViewSubject implements GenericSceneController {

    private Map<Integer, IslandNode> islandList;
    private final int cardID;
    private final Expert3 card;

    @FXML
    AnchorPane ParentNode;
    @FXML
    ChoiceBox IslandSelector;
    @FXML
    StackPane Island;
    @FXML
    Button chooseIsland;

    public Expert3SceneController( int cardID, Expert3 card) {
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

