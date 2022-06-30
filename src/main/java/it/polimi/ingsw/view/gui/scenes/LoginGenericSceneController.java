package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class LoginGenericSceneController extends ViewSubject implements GenericSceneController {

    @FXML
    TextField serverIPField;
    @FXML
    TextField serverPortField;
    @FXML
    Button connectionButton;

    @FXML
    public void initialize() {
        connectionButton.setOnAction(actionEvent -> connectionButtonClick());

        connectionButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                connectionButtonClick();
        });
    }

    private void connectionButtonClick() {

        HashMap<String, String> connectionInfo = new HashMap<>();
        connectionInfo.put("address", serverIPField.getText());
        connectionInfo.put("port", serverPortField.getText());
        new Thread(() -> notifyListener(l -> l.connectionRequest(connectionInfo))).start();


    }

    @Override
    public void close() {

    }
}
