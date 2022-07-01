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
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AssistantCardsController extends ViewSubject implements GenericSceneController, Initializable {

    ArrayList<AssistantCard> deck;
    Map<String, AssistantCard> lastCard;
    @FXML
    AnchorPane anchorPane;
    @FXML
    HBox lastCardHbox;
    @FXML
    TilePane tilePane;

    public AssistantCardsController() {
        this.deck = new ArrayList<>();
        this.lastCard = new HashMap<>();
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cardID = 0;

        tilePane.setHgap(10);
        tilePane.setVgap(10);

        for (AssistantCard card : deck) {
            ImageView imageView = new ImageView(new Image(card.getFrontImage(), 150.0, 150.0, true, false));
            imageView.setId(String.valueOf(cardID));
            tilePane.getChildren().add(imageView);

            if (!lastCard.containsValue(card))
                imageView.setOnMouseReleased(mouseEvent -> {
                    selectCard(Integer.parseInt(imageView.getId()));
                    imageView.setDisable(true);
                    mouseEvent.consume();
                });
            else
                imageView.setOpacity(0.5);

            cardID++;
        }

        for (String nickName : lastCard.keySet()) {
            AssistantCard card = lastCard.get(nickName);

            StackPane stackPane = new StackPane();
            ImageView imageView = new ImageView(new Image(card.getFrontImage(), 110.0, 110.0, true, false));
            stackPane.setPrefHeight(imageView.getY());
            stackPane.setPrefWidth(imageView.getX());
            Label player = new Label(nickName);
            player.setFont(new Font("Goudy Old Style", 12));
            stackPane.getChildren().addAll(
                    imageView,
                    player
                    );

            lastCardHbox.getChildren().add(stackPane);
        }

    }

    public void selectCard(int cardID) {
        Logger.getLogger("ASSISTANT CARD").info("Played card"+cardID);
        new Thread(()->notifyListener(list -> list.playAssistantCard(cardID))).start();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    public void setDeck(ArrayList<AssistantCard> deck) {
        this.deck = deck;
    }

    public void setLastCard(Map<String, AssistantCard> lastCard) {
        this.lastCard = lastCard;
    }

    @Override
    public void close() {

    }
}
