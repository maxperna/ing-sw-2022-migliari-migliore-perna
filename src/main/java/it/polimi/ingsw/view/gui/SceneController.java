package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.scenes.SceneControllerInt;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import jdk.jfr.Event;

import java.io.IOException;

public class SceneController extends ViewSubject {
    public static Scene currentScene;
    public static SceneControllerInt currentController;

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static SceneControllerInt getCurrentController() {
        return currentController;
    }

    /**Method to change the current main scene
     * @param controller controller of the scene
     * @param scene scene to use
     * @param FXMLpath path of the FXML path*/
    public static void changeRootScene(SceneControllerInt controller, Scene scene,String FXMLpath){
        currentController = controller;
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/"+FXMLpath));
        loader.setController(controller);
        currentController = controller;
        Parent root = loader.getRoot();

        currentScene = scene;
        currentScene.setRoot(root);

    }
}

