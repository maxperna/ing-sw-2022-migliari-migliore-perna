package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.DeckType.*;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.T;

public class TowerDeckSelectionControllerGeneric extends ViewSubject implements GenericSceneController, Initializable {

    @FXML
    BorderPane borderPane;
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

        Map<DeckType, ImageView> deckTypeImageViewMap = generateDeckTypeMap();
        Map<TowerColor, ImageView> towerColorMap = generateTowerColorMap();

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
                            else {
                                blackTower.setOpacity(0.8);
                                for (TowerColor resetColor : towerColorMap.keySet())
                                    if(!resetColor.equals(TowerColor.BLACK))
                                        towerColorMap.get(resetColor).setOpacity(1);
                            }
                            towerSelection(TowerColor.BLACK);
                        });
                        break;
                    case WHITE:
                        whiteTower.setOnMouseClicked(mouseEvent -> {
                            if (selectedColor == TowerColor.WHITE)
                                whiteTower.setOpacity(1);
                            else {
                                whiteTower.setOpacity(0.8);
                                for (TowerColor resetColor : towerColorMap.keySet())
                                    if(!resetColor.equals(TowerColor.WHITE))
                                        towerColorMap.get(resetColor).setOpacity(1);
                            }

                            towerSelection(TowerColor.WHITE);
                        });
                        break;
                    case GRAY:
                        grayTower.setOnMouseClicked(mouseEvent -> {
                            if (selectedColor == TowerColor.GRAY)
                                grayTower.setOpacity(1);
                            else {
                                grayTower.setOpacity(0.8);
                                for (TowerColor resetColor : towerColorMap.keySet())
                                    if(!resetColor.equals(TowerColor.GRAY))
                                        towerColorMap.get(resetColor).setOpacity(1);
                            }
                            towerSelection(TowerColor.GRAY);
                        });
                }
            } else {
                switch (color) {
                    case BLACK:
                        blackTower.setOpacity(0.2);
                        towerColorMap.remove(TowerColor.BLACK);
                        break;
                    case WHITE:
                        towerColorMap.remove(TowerColor.WHITE);
                        whiteTower.setOpacity(0.2);
                        break;
                    case GRAY:
                        grayTower.setOpacity(0.2);
                        towerColorMap.remove(TowerColor.GRAY);
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
                            else {
                                sageDeck.setOpacity(0.8);
                                for (DeckType resetType : deckTypeImageViewMap.keySet())
                                    if(!resetType.equals(SAGE))
                                        deckTypeImageViewMap.get(resetType).setOpacity(1);
                            }
                            deckSelection(SAGE);
                        });
                        break;
                    case WITCH:
                        witchDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == WITCH)
                                witchDeck.setOpacity(1);
                            else {
                                witchDeck.setOpacity(0.8);
                                for (DeckType resetType : deckTypeImageViewMap.keySet())
                                    if(!resetType.equals(WITCH))
                                        deckTypeImageViewMap.get(resetType).setOpacity(1);
                            }

                            deckSelection(WITCH);
                        });
                        break;
                    case KING:
                        kingDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == KING)
                                kingDeck.setOpacity(1);
                            else {
                                kingDeck.setOpacity(0.8);
                                for (DeckType resetType : deckTypeImageViewMap.keySet())
                                    if(!resetType.equals(KING))
                                        deckTypeImageViewMap.get(resetType).setOpacity(1);
                            }
                            deckSelection(KING);
                        });
                        break;
                    case DRUID:
                        druidDeck.setOnMouseClicked(mouseEvent -> {
                            if (selectedDeck == DRUID)
                                druidDeck.setOpacity(1);
                            else {
                                druidDeck.setOpacity(0.8);
                                for (DeckType resetType : deckTypeImageViewMap.keySet())
                                    if(!resetType.equals(DRUID))
                                        deckTypeImageViewMap.get(resetType).setOpacity(1);
                            }
                            deckSelection(DRUID);
                        });
                }
            } else {
                switch (deckType) {
                    case SAGE:
                        sageDeck.setOpacity(0.2);
                        deckTypeImageViewMap.remove(SAGE);
                        break;
                    case WITCH:
                        witchDeck.setOpacity(0.2);
                        deckTypeImageViewMap.remove(WITCH);
                        break;
                    case KING:
                        kingDeck.setOpacity(0.2);
                        deckTypeImageViewMap.remove(KING);
                        break;
                    case DRUID:
                        deckTypeImageViewMap.remove(DRUID);
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

    public void opacityHandler(TowerColor color) {

    }

    public void setRemainingTowers(ArrayList<TowerColor> remainingTowers) {
        this.remainingTowers = remainingTowers;
    }

    public void setRemainingDecks(ArrayList<DeckType> remainingDecks) {
        this.remainingDecks = remainingDecks;
    }

    private Map<TowerColor, ImageView> generateTowerColorMap() {

        Map<TowerColor, ImageView> towerColorImageViewMap = new HashMap<>();

        towerColorImageViewMap.put(TowerColor.BLACK, blackTower);
        towerColorImageViewMap.put(TowerColor.WHITE, whiteTower);
        towerColorImageViewMap.put(TowerColor.GRAY, grayTower);

        return  towerColorImageViewMap;
    }

    private Map<DeckType, ImageView> generateDeckTypeMap() {
        Map<DeckType, ImageView> deckTypeImageViewMap = new HashMap<>();

        deckTypeImageViewMap.put(DRUID, druidDeck);
        deckTypeImageViewMap.put(SAGE, sageDeck);
        deckTypeImageViewMap.put(KING, kingDeck);
        deckTypeImageViewMap.put(WITCH, witchDeck);

        return deckTypeImageViewMap;
    }

}
