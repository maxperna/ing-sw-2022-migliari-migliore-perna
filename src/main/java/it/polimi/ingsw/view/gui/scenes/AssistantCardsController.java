package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AssistantCardsController extends ViewSubject implements GenericSceneController, Initializable {

    ArrayList<AssistantCard> deck;
    Map<String, AssistantCard> lastCard;
    boolean isReady;
    @FXML
    AnchorPane anchorPane;

    public AssistantCardsController() {
        this.deck = new ArrayList<>();
        this.lastCard = new HashMap<>();
        isReady = false;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cardID = 1;
        HBox deckSpace = new HBox();
        deckSpace.setAlignment(Pos.CENTER);
        deckSpace.setSpacing(1.4);

        HBox lastCardSpace = new HBox();
        lastCardSpace.setAlignment(Pos.CENTER_LEFT);
        anchorPane.getChildren().add(lastCardSpace);
        AnchorPane.setBottomAnchor(lastCardSpace, 151.0);
        AnchorPane.setRightAnchor(lastCardSpace, 0.0);
        AnchorPane.setLeftAnchor(lastCardSpace, 0.0);
        AnchorPane.setTopAnchor(lastCardSpace, 0.0);

        anchorPane.getChildren().add(deckSpace);
        AnchorPane.setBottomAnchor(deckSpace, 0.0);
        AnchorPane.setRightAnchor(deckSpace, 0.0);
        AnchorPane.setLeftAnchor(deckSpace, 0.0);
        AnchorPane.setTopAnchor(deckSpace, 150.0);

        for (AssistantCard card : deck) {
            ImageView imageView = new ImageView(new Image(card.getFrontImage(), 140.0, 140.0, true, false));
            imageView.setId(String.valueOf(cardID));
            deckSpace.getChildren().add(imageView);

            if (!lastCard.containsValue(card))
                imageView.setOnMouseReleased(mouseEvent -> {
                    selectCard(Integer.parseInt(imageView.getId()));
                    imageView.setDisable(true);
                });
            else
                imageView.setOpacity(0.5);

            cardID++;
        }

        for (String nickName : lastCard.keySet()) {
            AssistantCard card = lastCard.get(nickName);

            StackPane stackPane = new StackPane();
            ImageView imageView = new ImageView(new Image(card.getFrontImage(), 100.0, 100.0, true, false));
            stackPane.setPrefHeight(imageView.getY());
            stackPane.setPrefWidth(imageView.getX());

            stackPane.getChildren().addAll(
                    imageView,
                    new Label(nickName));

            lastCardSpace.getChildren().add(stackPane);
        }

    }

    public void selectCard(int cardID) {
        new Thread(()->notifyListener(list -> list.playAssistantCard(cardID))).start();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    public void setDeck(ArrayList<AssistantCard> deck) {
        this.deck = deck;
        isReady = true;
    }

    public void setLastCard(Map<String, AssistantCard> lastCard) {
        this.lastCard = lastCard;
    }
}
