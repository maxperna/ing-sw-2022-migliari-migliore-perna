package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observer.ViewListener;
import it.polimi.ingsw.observer.ViewSubject;
import it.polimi.ingsw.view.gui.scenes.GenericSceneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneController extends ViewSubject {
    public static Scene currentScene;
    public static GenericSceneController currentController;
    public static GenericSceneController popUpController;
    public static GenericSceneController expertController;

    public static Scene currentPopUpScene;

    public static void changeRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();
        try {

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));
            Parent newRoot = loader.load();

            currentController = controller;
            currentScene.setRoot(newRoot);

            if(popUpController != null)
                popUpController.close();

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

            if(popUpController != null)
                popUpController.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void showNewPopUp(List<ViewListener> observerList, GenericSceneController controller, PopUpType type, String FXML_path, String title) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);
            if(type == PopUpType.DEFAULT)
                popUpController = controller;
            else
                expertController = controller;

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle(title);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("CSS/PopUpStyle.css");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setResizable(true);
            currentPopUpScene = scene;
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showNewPopUp(List<ViewListener> observerList, GenericSceneController controller, PopUpType type, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);
            if(type == PopUpType.DEFAULT)
                popUpController = controller;
            else
                expertController = controller;

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("CSS/PopUpStyle.css");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            currentPopUpScene = scene;
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void changePopUpRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);
            Parent newRoot = loader.load();
            popUpController = controller;
            currentPopUpScene.setRoot(newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showMessage(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        Thread thread = new Thread(() -> {
            try {
                // Wait for 1 sec
                Thread.sleep(1000);
                if (alert.isShowing()) {
                    Platform.runLater(alert::close);
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        alert.showAndWait();
    }

    public static void setFullScreen() {
        ((Stage) currentScene.getWindow()).setFullScreen(true);
    }

    public static void setCurrentScene(Scene currentScene) {
        SceneController.currentScene = currentScene;
    }

    public static void setCurrentController(GenericSceneController currentController) {
        SceneController.currentController = currentController;
    }

    public static synchronized void playSound() {

        List<String> audioFiles = new ArrayList<>();
        File folder = new File("src/main/resources/audio");
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && listOfFile.getName().contains(".wav")) {
                    audioFiles.add(listOfFile.getName());
                }
            }
            Collections.shuffle(audioFiles);

            new Thread(() -> {
                try {
                    Clip sound = AudioSystem.getClip();
                    sound.open(AudioSystem.getAudioInputStream(new File("src/main/resources/audio/" + audioFiles.get(0))));
                    sound.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        }
    }

}

