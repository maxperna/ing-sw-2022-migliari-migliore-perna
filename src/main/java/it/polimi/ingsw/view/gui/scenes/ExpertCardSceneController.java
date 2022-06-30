package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class ExpertCardSceneController extends ViewSubject implements GenericSceneController {
    @FXML
    ImageView exp1IMG;
    @FXML
    ImageView exp2IMG;
    @FXML
    ImageView exp3IMG;
    @FXML
    Label cost1;
    @FXML
    Label cost2;
    @FXML
    Label cost3;
    @FXML
    Label description1;
    @FXML
    Label description2;
    @FXML
    Label description3;

    private ArrayList<ExpertCard> expertCards;

    @FXML
    public void initialize(){
        //Image setting
        exp1IMG.setImage(new Image(expertCards.get(0).getIMG()));
        exp1IMG.setId("0");
        exp1IMG.setOnMousePressed(this::playExpert);

        exp2IMG.setImage(new Image(expertCards.get(1).getIMG()));
        exp1IMG.setId("1");
        exp1IMG.setOnMousePressed(this::playExpert);

        exp3IMG.setImage(new Image(expertCards.get(2).getIMG()));
        exp1IMG.setId("2");
        exp1IMG.setOnMousePressed(this::playExpert);
        //Cost setting
        cost1.setText("Cost: "+expertCards.get(0).getCost());
        cost2.setText("Cost: "+expertCards.get(1).getCost());
        cost3.setText("Cost: "+expertCards.get(2).getCost());
        //Description setting
        description1.setText(expertCards.get(0).getExpDescription());
        description2.setText(expertCards.get(1).getExpDescription());
        description3.setText(expertCards.get(2).getExpDescription());
    }


    public void playExpert(MouseEvent event){
        int cardID = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());
        event.consume();
        new Thread(()->notifyListener(list-> list.applyExpertEffect(cardID))).start();

    }
    public void setExpertCards(ArrayList<ExpertCard> expertCards) {
        this.expertCards = expertCards;
    }

    @Override
    public void close() {

    }
}
