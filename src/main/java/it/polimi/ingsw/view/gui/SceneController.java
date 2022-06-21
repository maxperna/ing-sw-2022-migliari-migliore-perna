package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.scenes.GenericSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

public class SceneController extends ViewSubject {
    public static Scene currentScene;
    public static GenericSceneController currentController;

    public static void changeRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();
        try {

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));
            Parent newRoot = loader.load();

            currentController = controller;
            currentScene.setRoot(newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void changeRoot(List<ViewListener> observerList, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

        try {
            Parent newRoot = loader.load();
            GenericSceneController controller = loader.getController();
            ((ViewSubject) controller).addAllListeners(observerList);
            currentController = controller;

            currentScene.setRoot(newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addPopUp(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {

    }


    public static void setCurrentScene(Scene currentScene) {
        SceneController.currentScene = currentScene;
    }

    public static void setCurrentController(GenericSceneController currentController) {
        SceneController.currentController = currentController;
    }

}

