package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.scenes.GenericSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    public static void showNewStage(List<ViewListener> observerList, GenericSceneController controller, String FXML_path, String title) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This window cannot be closed");
                alert.showAndWait();
                event.consume();
            });

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void setCurrentScene(Scene currentScene) {
        SceneController.currentScene = currentScene;
    }

    public static void setCurrentController(GenericSceneController currentController) {
        SceneController.currentController = currentController;
    }

}

