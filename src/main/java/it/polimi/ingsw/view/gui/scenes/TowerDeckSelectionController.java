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
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.model.DeckType.*;

public class TowerDeckSelectionController extends ViewSubject implements SceneControllerInt, Initializable {

    private ArrayList<TowerColor> remainingTowers;
    private ArrayList<DeckType> remainingDecks;
    private TowerColor selectedColor;
    private DeckType selectedDeck;
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

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedColor = null;
        selectedDeck = null;
        confirmButton.setOnAction(actionEvent -> confirmSelection());
        confirmButton.setOnKeyPressed(keyEvent -> {
            if( keyEvent.getCode() == KeyCode.ENTER)
                confirmSelection();
        });

        for(TowerColor color : TowerColor.values()) {
            if(remainingTowers.contains(color)) {
                switch (color) {
                    case BLACK:
                        blackTower.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                        break;
                    case WHITE:
                        whiteTower.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                        break;
                    case GRAY:
                        grayTower.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                }
            }
            else {
                switch (color) {
                    case BLACK:
                        blackTower.setOpacity(0.5);
                        break;
                    case WHITE:
                        whiteTower.setOpacity(0.5);
                        break;
                    case GRAY:
                        grayTower.setOpacity(0.5);
                }
            }
        }

        for(DeckType deckType : DeckType.values()) {
            if(remainingDecks.contains(deckType)) {
                switch (deckType) {
                    case SAGE:
                        sageDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                        break;
                    case WITCH:
                        witchDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                        break;
                    case KING:
                        kingDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                        break;
                    case DRUID:
                        druidDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selection);
                }
            }
            else {
                switch (deckType) {
                    case SAGE:
                        sageDeck.setOpacity(0.5);
                        break;
                    case WITCH:
                        witchDeck.setOpacity(0.5);
                        break;
                    case KING:
                        kingDeck.setOpacity(0.5);
                        break;
                    case DRUID:
                        druidDeck.setOpacity(0.5);
                }
            }
        }
    }

    public void selection(MouseEvent event){

        ImageView source = (ImageView) event.getSource();
        ImageView clickedNode = (ImageView) event.getPickResult().getIntersectedNode();
        clickedNode.setVisible(true);

        if(source.equals(blackTower)) {
            selectedColor = fromClassToTower(source);
        }
        if(source.equals(whiteTower)) {
            selectedColor = fromClassToTower(source);
        }
        if(source.equals(grayTower)) {
            selectedColor = fromClassToTower(source);
        }

        if(source.equals(sageDeck)) {
            selectedDeck = fromClassToDeckType(source);
        }
        if(source.equals(witchDeck)) {
            selectedDeck = fromClassToDeckType(source);
        }
        if(source.equals(kingDeck)) {
            selectedDeck = fromClassToDeckType(source);
        }
        if (source.equals(druidDeck)) {
            selectedDeck = fromClassToDeckType(druidDeck);
        }
    }

    public void confirmSelection() {

        if(selectedDeck == null && selectedColor == null)
            errorMessage.setText("Select a deck type and a tower color");
        else if(selectedDeck == null)
            errorMessage.setText("Select a deck type");
        else if(selectedColor == null)
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

    private TowerColor fromClassToTower(ImageView convert) {
        if(convert.equals(blackTower))
            return TowerColor.BLACK;
        else if(convert.equals(whiteTower))
            return TowerColor.WHITE;
        else if(convert.equals(grayTower))
            return TowerColor.GRAY;
        else
            return TowerColor.EMPTY;
    }

    private DeckType fromClassToDeckType(ImageView convert) {
        if(convert.equals(sageDeck))
            return SAGE;
        else if(convert.equals(witchDeck))
            return WITCH;
        else if(convert.equals(kingDeck))
            return KING;
        else
            return DRUID;
    }


}
