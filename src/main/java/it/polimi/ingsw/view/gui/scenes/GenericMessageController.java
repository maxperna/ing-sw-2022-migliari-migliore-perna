package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class used to manage the messageBox showing a received message
 */
public class GenericMessageController extends ViewSubject implements GenericSceneController{
    @FXML
    Label messageBox;
    private final String message;

    /**
     * Default constructor
     * @param message is the received message
     */
    public GenericMessageController(String message){
        this.message = message;
    }

    /**
     * Method used to set the messageBBox
     */
    @FXML
    public void initialize(){
        messageBox.setText(message);
    }

    @Override
    public void close() {

    }
}
