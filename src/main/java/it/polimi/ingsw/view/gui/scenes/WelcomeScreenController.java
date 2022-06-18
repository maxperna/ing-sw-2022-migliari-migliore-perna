package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class WelcomeScreenController extends ViewSubject implements SceneControllerInt {

    @FXML
    Button startButton;

    @FXML
    public void initialize(){
        startButton.addEventHandler(MouseEvent.MOUSE_PRESSED,this::startButtonClick);
    }

    public void startButtonClick(Event event){
        SceneController.changeMainPane(getList(), event,"LogInScene.fxml");
    }
}
