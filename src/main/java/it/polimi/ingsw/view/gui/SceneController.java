package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.scenes.SceneControllerInt;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

public class SceneController extends ViewSubject {
    public static Scene currentScene;
    public static SceneControllerInt currentController;

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static SceneControllerInt getCurrentController() {
        return currentController;
    }

    /**
     * Method to change the current main scene
     *
     * @param controller controller of the scene
     * @param scene      scene to use
     * @param FXMLpath   path of the FXML path
     */
    public static void changeMainPane(SceneControllerInt controller, Scene scene, String FXMLpath) {
        currentController = controller;
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + FXMLpath));
        loader.setController(controller);
        currentController = controller;
        Parent root = loader.getRoot();

        currentScene = scene;
        currentScene.setRoot(root);

    }

    public static void changeRoot(SceneControllerInt controllerInt, String FXML_path){

    }

    public static <T> T changeMainPane(List<ViewListener> observerList, String fxml) {
        return changeMainPane(observerList, currentScene, fxml);
    }
    public static <T> T changeMainPane(List<ViewListener> observerList, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeMainPane(observerList, scene, fxml);
    }


    public static <T> T changeMainPane(List<ViewListener> observerList, Scene scene, String fxml) {
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewSubject) controller).addAllListeners(observerList);

            currentController = (SceneControllerInt) controller;
            currentScene = scene;
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Client.LOGGER.severe(e.getMessage());
        }
        return controller;
    }

    public static void setCurrentScene(Scene currentScene) {
        SceneController.currentScene = currentScene;
    }

    public static void setCurrentController(SceneControllerInt currentController) {
        SceneController.currentController = currentController;
    }
}

