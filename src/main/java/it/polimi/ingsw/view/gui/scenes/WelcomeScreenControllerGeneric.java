package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.io.File;

public class WelcomeScreenControllerGeneric extends ViewSubject implements GenericSceneController {

    @FXML
    Button startButton;

    List<String> audioFiles;

    public WelcomeScreenControllerGeneric()
    {
        this.audioFiles = new ArrayList<>();
    }

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
        SceneController.playSound();
        Platform.runLater(() -> SceneController.changeRoot(list, "LogInScene.fxml"));
    }

    @Override
    public void close() {

    }
}
