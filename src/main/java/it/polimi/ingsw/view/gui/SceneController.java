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
import java.util.logging.Logger;

/**
 * Class used to manage the transitons between different scenes
 */
public class SceneController extends ViewSubject {
    public static Scene currentScene;
    public static GenericSceneController currentController;
    public static Scene currentPopUpScene;
    public static GenericSceneController popUpController;
    public static Scene currentExpertScene;
    public static GenericSceneController expertController;


    /**
     * Method used to change the root panel
     */
    public static void changeRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {
        actualChangeRoot(observerList, controller, FXML_path, ChangeType.DEFAULT);
    }

    /**
     * Method used to change the root panel
     */
    public static void changeRoot(List<ViewListener> observerList, String FXML_path) {
        actualChangeRoot(observerList, null, FXML_path, ChangeType.DEFAULT);
    }

    public static void changePopUpRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {
        actualChangeRoot(observerList, controller, FXML_path, ChangeType.POPUP);
    }

    public static void changeExpertRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path) {
        actualChangeRoot(observerList, controller, FXML_path, ChangeType.EXPERT);
    }

    /**
     * Method used to show a pop up message
     */
    public static void showNewPopUp(List<ViewListener> observerList, GenericSceneController controller, ChangeType type, String FXML_path, String title) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);

            if(type == ChangeType.POPUP)
                popUpController = controller;
            else if(type == ChangeType.EXPERT)
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

            if(type == ChangeType.POPUP)
                currentPopUpScene = scene;
            else if(type == ChangeType.EXPERT)
                currentExpertScene = scene;
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method used to show a pop up message
     */
    public static void showNewPopUp(List<ViewListener> observerList, GenericSceneController controller, ChangeType type, String FXML_path) {

        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            ((ViewSubject) controller).addAllListeners(observerList);
            loader.setController(controller);

            if(type == ChangeType.POPUP)
                popUpController = controller;
            else if(type == ChangeType.EXPERT)
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

            if(type == ChangeType.POPUP)
                currentPopUpScene = scene;
            else if(type == ChangeType.EXPERT)
                currentExpertScene = scene;

            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method used to show a message
     */
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

    private static void actualChangeRoot(List<ViewListener> observerList, GenericSceneController controller, String FXML_path, ChangeType type) {

        GenericSceneController actualController;
        Parent newRoot;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneController.class.getResource("/fxml/" + FXML_path));

            if(controller == null) {
                newRoot = loader.load();
                actualController = loader.getController();
                ((ViewSubject) actualController).addAllListeners(observerList);
            }
            else {
                actualController = controller;
                ((ViewSubject) actualController).addAllListeners(observerList);
                loader.setController(controller);
                newRoot = loader.load();
            }


            switch (type) {
                case DEFAULT:
                    currentController = actualController;
                    if(currentScene != null)
                        currentScene.setRoot(newRoot);
                    else
                        Logger.getLogger("SceneControllerError").warning("You did not set a scene");
                    break;
                case POPUP:
                    popUpController = actualController;
                    if(currentPopUpScene != null)
                        currentPopUpScene.setRoot(newRoot);
                    else
                        Logger.getLogger("SceneControllerError").warning("You did not set a scene");
                    break;
                case EXPERT:
                    expertController = actualController;
                    if(currentExpertScene != null)
                        currentExpertScene.setRoot(newRoot);
                    else
                        Logger.getLogger("SceneControllerError").warning("You did not set a scene");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFullScreen() {
        ((Stage) currentScene.getWindow()).setFullScreen(true);
    }

    /**
     * Method used to set a scene
     * @param currentScene scene the will be set
     */
    public static void setCurrentScene(Scene currentScene) {
        SceneController.currentScene = currentScene;
    }

    public static void setCurrentController(GenericSceneController currentController) {
        SceneController.currentController = currentController;
    }

    /**
     * Method used to play a sound
     */
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

