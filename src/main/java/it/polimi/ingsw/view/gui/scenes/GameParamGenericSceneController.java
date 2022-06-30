package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.PopUpType;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class GameParamGenericSceneController extends ViewSubject implements GenericSceneController {

    @FXML
    ChoiceBox<String> numOfPlayers;
    @FXML
    CheckBox expertMode;
    @FXML
    TextArea expModInfo;
    @FXML
    Button startGame;

    @FXML
    public void initialize() {

        numOfPlayers.setItems(FXCollections.observableArrayList("Two", "Three", "Four"));
        expertMode.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, this::setExpertModeInfo);
        expertMode.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, this::setExpertModeInfo);
        startGame.setOnAction(actionEvent -> startGame());
        startGame.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                startGame();
        });
    }

    /**
     * Method to show the hidden expert mode info
     */
    private void setExpertModeInfo(Event event) {
        expModInfo.setVisible(!expModInfo.isVisible());
    }

    private void startGame() {
        int playersNum;
        if (numOfPlayers.getSelectionModel().getSelectedItem().equals(numOfPlayers.getItems().get(0)))
            playersNum = 2;
        else if (numOfPlayers.getSelectionModel().getSelectedItem().equals(numOfPlayers.getItems().get(1)))
            playersNum = 3;
        else
            playersNum = 4;

        boolean expMode = expertMode.isSelected();

        startGame.setDisable(true);
        expertMode.setDisable(true);
        numOfPlayers.setDisable(true);
        new Thread(() -> notifyListener(l -> l.sendGameParam(playersNum, expMode))).start();

        Platform.runLater(() -> SceneController.showNewStage(list, new VeryImportantController(), PopUpType.DEFAULT, "VeryImportantPopUPp.fxml"));
    }

    @Override
    public void close() {

    }
}
