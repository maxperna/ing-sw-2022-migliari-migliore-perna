package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class JavaFXGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Gui view = new Gui();
        ClientController clientController = new ClientController(view);
        //Bisogna aggiungere anche degli observer

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/LogInScene.fxml"));

    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
