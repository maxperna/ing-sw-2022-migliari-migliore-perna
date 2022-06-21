package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class NicknameInputControllerGeneric extends ViewSubject implements GenericSceneController {

    @FXML
    TextField nickField;
    @FXML
    Button sendNick;
    @FXML
    Label errorOutPut;

    @FXML
    public void initialize(){
        sendNick.setOnAction(actionEvent -> sendNickButtonClick());
        sendNick.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                sendNickButtonClick();
        });
    }

    public void sendNickButtonClick(){

        String nick = nickField.getText();
        sendNick.setDisable(true);
        new Thread(() -> notifyListener(l -> l.sendNickname(nick))).start();

    }

    public void printErrorMessage (String error) {
        errorOutPut.setText(error);
    }
}
