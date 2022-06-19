package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;

public class LoginSceneController extends ViewSubject implements SceneControllerInt {

    @FXML
    TextField serverIPField;
    @FXML
    TextField serverPortField;
    @FXML
    Button connectionButton;

    @FXML
    public void initialize(){
        connectionButton.addEventHandler(MouseEvent.MOUSE_RELEASED,this::connectionButtonClick);
    }

    private void connectionButtonClick(Event event){


        HashMap<String, String> connectionInfo = new HashMap<>();
        connectionInfo.put("address", serverIPField.getText());
        connectionInfo.put("port", serverPortField.getText());
        new Thread(() -> notifyListener(l -> l.connectionRequest(connectionInfo))).start();


    }

}
