package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AssistantCardsController extends ViewSubject implements GenericSceneController, Initializable {

    ArrayList<AssistantCard> deck;
    Map<ImageView, Integer> imageViewIntegerMap;
    @FXML
    AnchorPane anchorPane;

    public AssistantCardsController(ArrayList<AssistantCard> deck) {
        this.deck = deck;
        imageViewIntegerMap = new HashMap<>();
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cardID = 1;
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(1.2);

        anchorPane.getChildren().add(hBox);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setTopAnchor(hBox, 150.0);

        for(AssistantCard card : deck) {
            ImageView imageView = new ImageView(new Image(card.getFrontImage(), 140.0, 140.0, true, false));
            hBox.getChildren().add(imageView);
            imageViewIntegerMap.put(imageView, cardID);
            imageView.setOnMouseReleased(mouseEvent -> selectCard(imageViewIntegerMap.get(imageView)));
        }

    }

    public void selectCard(int cardID) {
        this.notifyListener(list -> list.playAssistantCard(cardID));
    }

}
