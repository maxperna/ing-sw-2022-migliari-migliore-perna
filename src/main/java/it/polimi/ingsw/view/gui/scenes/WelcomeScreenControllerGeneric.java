package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class WelcomeScreenControllerGeneric extends ViewSubject implements GenericSceneController {

    @FXML
    Button startButton;

    @FXML
    public void initialize() {

        startButton.setOnAction(actionEvent -> startButtonClick());

        startButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                startButtonClick();
            }
        });
    }

    public void startButtonClick() {
        SceneController.changeRoot(list, "LogInScene.fxml");
    }

    @Override
    public void close() {

    }
}
