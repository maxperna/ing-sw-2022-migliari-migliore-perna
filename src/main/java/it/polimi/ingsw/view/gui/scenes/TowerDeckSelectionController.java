package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DeckType;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class TowerDeckSelectionController extends ViewSubject implements SceneControllerInt {

    private ArrayList<TowerColor> remainingTowers;

    private ArrayList<DeckType> remainingDecks;

    //Tower imageviewer
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

    public void initialize() {

    }

    public void setRemainingTowers(ArrayList<TowerColor> remainingTowers) {
        this.remainingTowers = remainingTowers;
    }

    public void setRemainingDecks(ArrayList<DeckType> remainingDecks) {
        this.remainingDecks = remainingDecks;
    }
}
