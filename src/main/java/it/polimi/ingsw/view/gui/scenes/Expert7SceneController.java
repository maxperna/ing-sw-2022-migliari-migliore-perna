package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.experts.Expert7;
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

public class Expert7SceneController extends ViewSubject implements GenericSceneController {
    @FXML
    AnchorPane ParentNode;
    @FXML
    HBox EntryRoom;
    @FXML
    HBox StudentsOnCard;
    @FXML
    Button SwapButton;

    private final ArrayList<Color> entryRoomSwap;
    private final ArrayList<Color> cardSwap;

    private final int cardID;
    private final Expert7 card;

    public Expert7SceneController(int cardID, Expert7 card){
        this.cardID = cardID;
        this.card = card;

        entryRoomSwap = new ArrayList<>();
        cardSwap = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        SwapButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::playExpert);
        for(Color student : card.getStudentsOnCard()){
            ImageView studToPlace = new ImageView(student.getStudImg());
            studToPlace.setId(student.toString());
            studToPlace.setFitWidth(46);
            studToPlace.setFitHeight(70);
            StudentsOnCard.getChildren().add(studToPlace);
            studToPlace.addEventHandler(MouseEvent.MOUSE_CLICKED,this::selectCard);
        }
        for(Color student : card.getEntryRoom()){
            ImageView studToPlace = new ImageView(student.getStudImg());
            studToPlace.setId(student.toString());
            studToPlace.setFitWidth(40);
            studToPlace.setFitHeight(65);
            EntryRoom.getChildren().add(studToPlace);

            studToPlace.addEventHandler(MouseEvent.MOUSE_CLICKED,this::selectEntry);
        }
    }

    /**Method to select students on entry room*/
    public void selectEntry(MouseEvent event){
        if(entryRoomSwap.size()<3) {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            selectedNode.setOpacity(0.8);
            entryRoomSwap.add(convertColor(selectedNode));
            event.consume();
        }

    }
    /**Method to select students on card*/
    public void selectCard(MouseEvent event){
        if(cardSwap.size()<3) {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            selectedNode.setOpacity(0.8);
            cardSwap.add(convertColor(selectedNode));
            event.consume();
        }
    }

    public void playExpert(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard5(cardID,cardSwap,entryRoomSwap))).start();
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
