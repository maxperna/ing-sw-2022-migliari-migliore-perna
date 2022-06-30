package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VeryImportantController extends ViewSubject implements GenericSceneController {
    @FXML
    AnchorPane anchorPane;

    public void initialize() {

    }

    public void close() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
