package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class WelcomeScreenController extends ViewSubject implements SceneControllerInt {

    @FXML
    Button startButton;

    @FXML
    public void initialize(){

        startButton.setOnAction(actionEvent -> startButtonClick());

        startButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                startButtonClick();
            }
        });
    }

    public void startButtonClick(){
        SceneController.changeRoot(list,"LogInScene.fxml");
    }
}
