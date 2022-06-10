package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observer.ViewSubject;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class GameParamScene extends ViewSubject implements SceneControllerInt {

    @FXML
    ChoiceBox<String> numOfPlayers;
    @FXML
    CheckBox expertMode;
    @FXML
    TextArea expModInfo;
    @FXML
    Button startGame;

    @FXML
    public void initialize(){

        numOfPlayers.setItems(FXCollections.observableArrayList("Two","Three","Four"));
        expertMode.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,this::setExpertModeInfo);
        expertMode.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,this::setExpertModeInfo);
        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED,this::startGame);
    }

    /**Method to show the hidden expert mode info*/
    @FXML
    public void setExpertModeInfo(Event event){
        expModInfo.setVisible(!expModInfo.isVisible());
    }

    public void startGame(Event event){
        int playersNum;
        if(numOfPlayers.getSelectionModel().getSelectedItem().equals(numOfPlayers.getItems().get(0)))
            playersNum =2;
        else if (numOfPlayers.getSelectionModel().getSelectedItem().equals(numOfPlayers.getItems().get(1)) )
            playersNum = 3;
        else
            playersNum= 4;

        boolean expMode = expertMode.isSelected();

        new Thread(()->notifyListener(l->l.sendGameParam(playersNum,expMode)));
    }
}
