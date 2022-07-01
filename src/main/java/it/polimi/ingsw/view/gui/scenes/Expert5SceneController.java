package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.experts.Expert5;
import it.polimi.ingsw.model.experts.Expert7;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Expert5SceneController extends ViewSubject implements GenericSceneController, Initializable {

    private Map<Integer, IslandNode> islandList;
    private ArrayList<Node> islandNodes;
    private final int cardID;
    private final Expert5 card;

    @FXML
    AnchorPane ParentNode;
    @FXML
    HBox EntryRoom;
    @FXML
    HBox StudentsOnCard;
    @FXML
    Button chooseIsland;

    public Expert5SceneController(Map<Integer, IslandNode> islandList, ArrayList<Node> islandNodes, int cardID, Expert5 card) {
        this.islandList = islandList;
        this.islandNodes = islandNodes;
        this.cardID = cardID;
        this.card = card;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseIsland.setOnAction(actionEvent -> {
            ChooseIslandSceneController chooseIslandSceneController = new ChooseIslandSceneController(islandList, islandNodes, cardID, card);
            SceneController.changeExpertRoot(list, chooseIslandSceneController, "ChooseIslandScene.fxml");
        });
    }

    @Override
    public void close() { }
}
