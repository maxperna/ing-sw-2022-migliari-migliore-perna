package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.experts.ExpertCard;
import it.polimi.ingsw.observer.ViewSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class controlling the panel showing the expert cards
 */
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

    @FXML
    Label alert1;
    @FXML
    Label alert2;
    @FXML
    Label alert3;

    private boolean expertPlayed;
    private final ArrayList<ExpertCard> expertCards;
    private final int numOfCoins;

    /**
     * Default controller
     * @param experts is an arraylist of available expert cards
     * @param expertPlayed is a boolean checking that the player hasn't already played the expert card
     * @param numOfCoins is the number of available coins
     */
    public ExpertCardSceneController(ArrayList<ExpertCard> experts,boolean expertPlayed,int numOfCoins ){
        this.expertCards = experts;
        this.expertPlayed = expertPlayed;
        this.numOfCoins = numOfCoins;
    }

    /**
     * Method used to initialize the panel, setting the events and the images
     */
    @FXML
    public void initialize(){

        //Image setting
        exp1IMG.setImage(new Image(expertCards.get(0).getIMG()));
        exp1IMG.setId("0");
        exp1IMG.setOnMousePressed(this::playExpert);

        exp2IMG.setImage(new Image(expertCards.get(1).getIMG()));
        exp2IMG.setId("1");
        exp2IMG.setOnMousePressed(this::playExpert);

        exp3IMG.setImage(new Image(expertCards.get(2).getIMG()));
        exp3IMG.setId("2");
        exp3IMG.setOnMousePressed(this::playExpert);
        //Cost setting
        cost1.setText("Cost: "+expertCards.get(0).getCost());
        cost2.setText("Cost: "+expertCards.get(1).getCost());
        cost3.setText("Cost: "+expertCards.get(2).getCost());
        //Description setting
        description1.setText(expertCards.get(0).getExpDescription());
        description2.setText(expertCards.get(1).getExpDescription());
        description3.setText(expertCards.get(2).getExpDescription());
    }

    /**
     * Method used to notify the chosen expert card
     * @param event is the registered event
     */
    public void playExpert(MouseEvent event){
        if(!expertPlayed) {
            int cardID = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());
            event.consume();
            if(expertCards.get(cardID).getCost()<=numOfCoins) {
                new Thread(() -> notifyListener(list -> list.applyExpertEffect(cardID))).start();
                close();
            }
            else{
                if(cardID == 0)
                    alert1.setVisible(true);
                else if(cardID ==1)
                    alert2.setVisible(true);
                else
                    alert3.setVisible(true);

                event.consume();
            }
        }
        else
            event.consume();

    }

    @Override
    public void close() {
        ((Stage) exp1IMG.getParent().getScene().getWindow()).close();
    }

    /**Method to set if is possible to play an expert card*/
    public void makeCardAvailable(boolean available){
        this.expertPlayed = available;
    }
}
