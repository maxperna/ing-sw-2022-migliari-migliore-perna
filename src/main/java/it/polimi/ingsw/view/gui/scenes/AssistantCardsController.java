package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AssistantCardsController extends ViewSubject implements GenericSceneController, Initializable {

    ArrayList<AssistantCard> deck;

    @FXML
    BorderPane borderPane;

    public AssistantCardsController(ArrayList<AssistantCard> deck) {
        this.deck = deck;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        for(AssistantCard card : deck){
//            ImageView img = new ImageView(card.getFrontImage());
//            img.fitWidthProperty().bind(new SimpleDoubleProperty(((Stage) borderPane.getScene().getWindow()).getWidth()));
//        }
    }

}
