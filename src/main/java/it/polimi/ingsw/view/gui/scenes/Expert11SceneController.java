package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.Expert11;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Expert11SceneController extends ViewSubject implements GenericSceneController {

    @FXML
    AnchorPane parentNode;
    @FXML
    HBox StudentSpace;

    private final int cardID;
    private final Expert11 card;

    public Expert11SceneController(int cardID,Expert11 card) {
        this.cardID = cardID;
        this.card = card;
    }

    @FXML
    public void initialize(){
        for(Color student:card.getStudentsOnCard()){
            ImageView studentIMG = new ImageView(student.getStudImg());
            studentIMG.setFitWidth(46);
            studentIMG.setFitHeight(70);
            studentIMG.setId(student.toString());
            studentIMG.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceStudent);
            StudentSpace.getChildren().add(studentIMG);
        }
    }

    public void choiceStudent(MouseEvent event){
        Node selectedColor = event.getPickResult().getIntersectedNode();
        event.consume();
        Color colorPicked;
        if(selectedColor.getId().equals(Color.RED.toString()))
            colorPicked = Color.RED;
        else if (selectedColor.getId().equals(Color.BLUE.toString()))
            colorPicked = Color.BLUE;

        else if (selectedColor.getId().equals(Color.GREEN.toString()))
            colorPicked = Color.GREEN;

        else if (selectedColor.getId().equals(Color.PINK.toString()))
            colorPicked = Color.PINK;
        else
            colorPicked = Color.YELLOW;

        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,colorPicked))).start();
        close();

    }

    @Override
    public void close() {
        ((Stage) parentNode.getScene().getWindow()).close();
    }
}
