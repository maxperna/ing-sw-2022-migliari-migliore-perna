package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.model.DeckType.*;

public class TowerDeckSelectionControllerGeneric extends ViewSubject implements GenericSceneController, Initializable {

    @FXML
    ImageView blackTower;
    @FXML
    ImageView whiteTower;
    @FXML
    ImageView grayTower;
    @FXML
    ImageView sageDeck;
    @FXML
    ImageView witchDeck;
    @FXML
    ImageView kingDeck;
    @FXML
    ImageView druidDeck;
    @FXML
    Button confirmButton;
    @FXML
    Label errorMessage;
    @FXML
    Label infoLabel;
    private ArrayList<TowerColor> remainingTowers;
    private ArrayList<DeckType> remainingDecks;
    private TowerColor selectedColor;
    private DeckType selectedDeck;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedColor = null;
        selectedDeck = null;
        confirmButton.setOnAction(actionEvent -> confirmSelection());
        confirmButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                confirmSelection();
        });

        for (TowerColor color : TowerColor.values()) {
            if (remainingTowers.contains(color)) {
                switch (color) {
                    case BLACK:
                        blackTower.setOnMouseClicked(mouseEvent -> {
                            if (selectedColor == TowerColor.BLACK)
                                blackTower.setOpacity(1);
                            else
                                blackTower.setOpacity(0.8);

                            towerSelection(TowerColor.BLACK);
                        });
                        break;
                    case WHITE:
                        whiteTower.setOnMouseClicked(mouseEvent -> {
                            if (selectedColor == TowerColor.WHITE)
                                whiteTower.setOpacity(1);
                            else
                                whiteTower.setOpacity(0.8);

                            towerSelection(TowerColor.WHITE);
                        });
                        break;
                    case GRAY:
                        grayTower.setOnMouseClicked(mouseEvent -> {
                            if (selectedColor == TowerColor.GRAY)
                                grayTower.setOpacity(1);
                            else
                                grayTower.setOpacity(0.8);

                            towerSelection(TowerColor.GRAY);
                        });
                }
            } else {
                switch (color) {
                    case BLACK:
                        blackTower.setOpacity(0.2);
                        break;
                    case WHITE:
                        whiteTower.setOpacity(0.2);
                        break;
                    case GRAY:
                        grayTower.setOpacity(0.2);
                }
            }
        }

        for (DeckType deckType : DeckType.values()) {
            if (remainingDecks.contains(deckType)) {
                switch (deckType) {
                    case SAGE:
                        sageDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == SAGE)
                                sageDeck.setOpacity(1);
                            else
                                sageDeck.setOpacity(0.8);

                            deckSelection(SAGE);
                        });
                        break;
                    case WITCH:
                        witchDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == WITCH)
                                witchDeck.setOpacity(1);
                            else
                                witchDeck.setOpacity(0.8);

                            deckSelection(WITCH);
                        });
                        break;
                    case KING:
                        kingDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == KING)
                                kingDeck.setOpacity(1);
                            else
                                kingDeck.setOpacity(0.8);

                            deckSelection(KING);
                        });
                        break;
                    case DRUID:
                        druidDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == DRUID)
                                druidDeck.setOpacity(1);
                            else
                                druidDeck.setOpacity(0.8);

                            deckSelection(DRUID);
                        });
                }
            } else {
                switch (deckType) {
                    case SAGE:
                        sageDeck.setOpacity(0.2);
                        break;
                    case WITCH:
                        witchDeck.setOpacity(0.2);
                        break;
                    case KING:
                        kingDeck.setOpacity(0.2);
                        break;
                    case DRUID:
                        druidDeck.setOpacity(0.2);
                }
            }
        }

    }


    public void towerSelection(TowerColor towerColor) {

        if (selectedColor == towerColor)
            selectedColor = null;
        else
            selectedColor = towerColor;
    }

    public void deckSelection(DeckType deckType) {

        if (selectedDeck == deckType)
            selectedDeck = null;
        else
            selectedDeck = deckType;
    }

    public void confirmSelection() {

        if (selectedDeck == null && selectedColor == null)
            errorMessage.setText("Select deck type and tower color");
        else if (selectedDeck == null)
            errorMessage.setText("Select a deck type");
        else if (selectedColor == null)
            errorMessage.setText("Select a tower color");
        else {
            blackTower.setDisable(true);
            whiteTower.setDisable(true);
            grayTower.setDisable(true);
            sageDeck.setDisable(true);
            witchDeck.setDisable(true);
            kingDeck.setDisable(true);
            druidDeck.setDisable(true);
            confirmButton.setDisable(true);
            errorMessage.setText("");
            infoLabel.setTextFill(Color.BLACK);
            infoLabel.setText("Waiting for other players...");
            new Thread(() -> notifyListener(list -> list.chooseTowerColorAndDeck(selectedColor, selectedDeck))).start();
        }
    }

    public void setRemainingTowers(ArrayList<TowerColor> remainingTowers) {
        this.remainingTowers = remainingTowers;
    }

    public void setRemainingDecks(ArrayList<DeckType> remainingDecks) {
        this.remainingDecks = remainingDecks;
    }


}
