package it.polimi.ingsw.model.experts;

import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.Random;


/**Factory singleton class for draw three assistant from the assistant cards deck, if three assistant are already been
 * called returns the three already generated
 * @author  Massimo
 * */
public class ExpertsFactory {

    private final Game currentGame;
    private static ExpertsFactory instance = null;
    ArrayList<ExpertCard> calledExpert;   //assistant already generated

    private ExpertsFactory(Game currentGame){
        this.currentGame = currentGame;
    }

    public static ExpertsFactory createFactory(Game currentGame){
        if(instance == null)
            instance = new ExpertsFactory(currentGame);

        return instance;
    }
    /**Assistant card drawer to pick randomly three assistant cards generating three different random number from 0 to 11,
     *
     * @return an array list of three ExpertCard
     * */
    private ArrayList<ExpertCard> generateExperts(){

        calledExpert = new ArrayList<>();
        ArrayList<Integer> numbersGenerated = new ArrayList<>();     //list containing numbers already generated
        Random randomGenerator = new Random();
        for(int i=0;i<3;i++){
            int randomDraw;
            do{
                randomDraw = randomGenerator.nextInt(12);
            }while(numbersGenerated.contains(randomDraw));         //check if the number is not already in the list
            numbersGenerated.add(randomDraw);

            ExpertCard generatedCard;

            switch (randomDraw){
                case 0:
                    generatedCard = new Expert1(this.currentGame);
                    break;
                case 1:
                    generatedCard = new Expert2(this.currentGame);
                    break;
                case 2:
                    generatedCard = new Expert3(this.currentGame);
                    break;
                case 3:
                    generatedCard = new Expert4();
                    break;
                case 4:
                    generatedCard = new Expert5(this.currentGame);
                    break;
                case 5:
                    generatedCard = new Expert6(this.currentGame);
                    break;
                case 6:
                    generatedCard = new Expert7(this.currentGame);
                    break;
                case 7:
                    generatedCard = new Expert8(currentGame);
                    break;
                case 8:
                    generatedCard = new Expert9(this.currentGame);
                    break;
                case 9:
                    generatedCard = new Expert10(currentGame);
                    break;
                case 10:
                    generatedCard = new Expert11(this.currentGame);
                    break;
                case 11:
                    generatedCard = new Expert12(this.currentGame);
                    break;
                default:
                    generatedCard = null;

            }
            calledExpert.add(generatedCard);
        }
        return calledExpert;
    }

    /**Method to implements singleton
     * @return a list of three ExpertCard or call the generator if it doesn't exist
     * */
    public ArrayList<ExpertCard> drawExperts(){
        if(calledExpert == null){
            return generateExperts();
        }
        else return calledExpert;
    }
}
