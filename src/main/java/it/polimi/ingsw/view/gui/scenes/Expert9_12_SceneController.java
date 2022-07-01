package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**Used for expert 12 as well*/
public class Expert9_12_SceneController extends ViewSubject implements GenericSceneController {
    @FXML
    AnchorPane ParentNode;
    @FXML
    ImageView Blue;
    @FXML
    ImageView Red;
    @FXML
    ImageView Yellow;
    @FXML
    ImageView Pink;
    @FXML
    ImageView Green;

    private final int cardID;

    public Expert9_12_SceneController(int cardID) {
        this.cardID = cardID;
    }

    @FXML
    public void initialize(){
        Blue.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceBlue);
        Red.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceRed);
        Yellow.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceYellow);
        Pink.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choicePink);
        Green.addEventHandler(MouseEvent.MOUSE_PRESSED,this::choiceGreen);
    }

    public void choiceRed(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,Color.RED))).start();
        close();
    }
    public void choiceBlue(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,Color.BLUE))).start();
        close();
    }
    public void choicePink(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,Color.PINK))).start();
        close();
    }
    public void choiceGreen(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,Color.GREEN))).start();
        close();
    }
    public void choiceYellow(MouseEvent event){
        event.consume();
        new Thread(()->notifyListener(l->l.playExpertCard4(cardID,Color.YELLOW))).start();
        close();
    }

    @Override
    public void close() {
        ((Stage) ParentNode.getScene().getWindow()).close();
    }
}
