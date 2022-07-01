package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.Expert10;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Expert10SceneController extends ViewSubject implements GenericSceneController {

    @FXML
    AnchorPane ParentNode;
    @FXML
    HBox EntryRoom;
    @FXML
    HBox DiningRoom;
    @FXML
    Button SwapButton;

    private final int cardID;
    private final Expert10 card;

    private final ArrayList<Color> swapEntry;
    private final ArrayList<Color> swapDining;

    public Expert10SceneController(int cardID, Expert10 card){
        this.cardID = cardID;
        this.card = card;
        swapEntry = new ArrayList<>();
        swapDining = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        SwapButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::playExpert);
        for(Color student : card.getEntryRoom()){
            ImageView studToPlace = new ImageView(student.getStudImg());
            studToPlace.setId(student.toString());
            studToPlace.setFitWidth(25);
            studToPlace.setFitHeight(45);
            EntryRoom.getChildren().add(studToPlace);
            studToPlace.addEventHandler(MouseEvent.MOUSE_CLICKED,this::selectEntry);
        }
        for(Color student : card.getDiningRoom().keySet()){
           if(card.getDiningRoom().get(student)!=0){
               ImageView studToPlace = new ImageView(student.getStudImg());
               studToPlace.setId(student.toString());
               studToPlace.setFitWidth(30);
               studToPlace.setFitHeight(55);
               EntryRoom.getChildren().add(studToPlace);
               studToPlace.addEventHandler(MouseEvent.MOUSE_CLICKED,this::selectDining);

            }


        }
    }

    public void selectDining(MouseEvent event){
        if(swapDining.size()<2) {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            selectedNode.setOpacity(0.8);
            swapDining.add(convertColor(selectedNode));
            event.consume();
        }

    }
    /**Method to select students on card*/
    public void selectEntry(MouseEvent event){
        if(swapEntry.size()<3) {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            selectedNode.setOpacity(0.8);
            swapEntry.add(convertColor(selectedNode));
            event.consume();
        }
    }

    public void playExpert(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard5(cardID,swapDining,swapEntry))).start();
        close();
    }

    private Color convertColor(Node selectedColor){

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

        return colorPicked;
    }
    @Override
    public void close() {
        ((Stage) ParentNode.getScene().getWindow()).close();
    }


}
