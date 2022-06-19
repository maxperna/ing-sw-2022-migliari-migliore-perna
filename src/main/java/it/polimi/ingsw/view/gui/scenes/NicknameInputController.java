package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NicknameInputController extends ViewSubject implements SceneControllerInt{

    @FXML
    TextField nickField;
    @FXML
    Button sendNick;

    @FXML
    public void initialize(){
        sendNick.addEventHandler(MouseEvent.MOUSE_RELEASED,this::sendNickButtonClick);
    }

    public void sendNickButtonClick(Event event){

        String nick = nickField.getText();
        sendNick.setDisable(true);
        new Thread(() -> notifyListener(l -> l.sendNickname(nick))).start();

    }
}
