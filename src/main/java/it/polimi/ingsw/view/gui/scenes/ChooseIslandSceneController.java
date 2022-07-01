package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.experts.Expert5;
import it.polimi.ingsw.model.experts.Expert7;
import it.polimi.ingsw.model.gameField.IslandNode;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class ChooseIslandSceneController extends ViewSubject implements GenericSceneController, Initializable {

    private Map<Integer, IslandNode> islandList;
    private ArrayList<Node> islandNodes;
    private final int cardID;
    private final Expert5 card;

    @FXML
    GridPane gridPane;

    public ChooseIslandSceneController(Map<Integer, IslandNode> islandList, ArrayList<Node> islandNodes, int cardID, Expert5 card) {
        this.islandList = islandList;
        this.islandNodes = islandNodes;
        this.cardID = cardID;
        this.card = card;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 0;
        int row = 0;

        for(Integer islandID : islandList.keySet()) {
            StackPane stackPane = ((StackPane)gridPane.lookup("#" + column + row));
            stackPane.getChildren().add(islandNodes.get(islandID-1));
            stackPane.setOnMouseClicked(mouseEvent -> {
                mouseEvent.getPickResult().getIntersectedNode().getId();
            });
            column ++;
            if(column > 2) {
                column = 0;
                row ++;
            }
        }
    }

   public void selectIsland(int nodeId) {
       new Thread(()->notifyListener(l->l.playExpertCard2(cardID, nodeId))).start();
       close();
   }


    @Override
    public void close() {
        ((Stage) gridPane.getScene().getWindow()).close();
    }
}
