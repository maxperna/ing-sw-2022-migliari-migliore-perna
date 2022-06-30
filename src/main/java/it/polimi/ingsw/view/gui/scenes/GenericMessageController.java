package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GenericMessageController extends ViewSubject implements GenericSceneController{
    @FXML
    Label messageBox;
    private final String message;

    public GenericMessageController(String message){
        this.message = message;
    }
    @FXML
    public void initialize(){
        messageBox.setText(message);
    }

    @Override
    public void close() {

    }
}
