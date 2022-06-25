package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AssistantCardsController extends ViewSubject implements GenericSceneController, Initializable {

    ArrayList<AssistantCard> deck;

    public AssistantCardsController(ArrayList<AssistantCard> deck) {
        this.deck = deck;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int numOfCards = 0;
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(50);

        for (AssistantCard card : deck) {
            final ImageView imageView = new ImageView(new Image(card.getFrontImage(), 100.0, 100.0, true, false));
            hBox.getChildren().add(imageView);
            numOfCards++;
        }


    }

}
