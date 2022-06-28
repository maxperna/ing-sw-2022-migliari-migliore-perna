package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.scenes.WelcomeScreenControllerGeneric;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Gui view = new Gui();
        ClientController clientController = new ClientController(view);
        view.addListener(clientController);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/WelcomeScene.fxml"));

        Parent rootLayout = null;
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            System.exit(1);
        }

        WelcomeScreenControllerGeneric controller = loader.getController();
        controller.addListener(clientController);
        SceneController.setCurrentController(controller);


        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        SceneController.setCurrentScene(scene);
        stage.setTitle("Eryantis");
        stage.setResizable(false);
        stage.show();


    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
