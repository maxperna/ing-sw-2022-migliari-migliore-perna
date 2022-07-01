package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * Class generating a panel that asks for player's nickname
 */
public class NicknameInputControllerGeneric extends ViewSubject implements GenericSceneController {

    @FXML
    TextField nickField;
    @FXML
    Button sendNick;
    @FXML
    Label errorOutPut;

    /**
     * Method used to set all the events
     */
    @FXML
    public void initialize() {
        sendNick.setOnAction(actionEvent -> sendNickButtonClick());
        sendNick.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                sendNickButtonClick();
        });
    }

    /**
     * Method used to send the acquired nickname
     */
    public void sendNickButtonClick() {

        String nick = nickField.getText();
        sendNick.setDisable(true);
        new Thread(() -> notifyListener(l -> l.sendNickname(nick))).start();

    }

    /**
     * Method used to print an error message
     * @param error is the error message
     */
    public void printErrorMessage(String error) {
        errorOutPut.setText(error);
    }

    @Override
    public void close() {

    }
}
