package it.polimi.ingsw.model.assistants;

import java.util.ArrayList;
import java.util.Random;


/**Factory singleton class for draw three assistant from the assistant cards deck, if three assistant are already been
 * called returns the three already generated
 * @author  Massimo
 * */
public class AssistantFactory {

    private static AssistantFactory instance = null;
    ArrayList<AssistantCard> calledAssistant;   //assistant already generated

    private AssistantFactory(){}

    public static AssistantFactory createFactory(){
        if(instance == null)
            instance = new AssistantFactory();

        return instance;
    }
    /**Assistant card drawer to pick randomly three assistant cards generating three different random number from 0 to 11,
     *
     * @return an array list of three AssistantCard
     * */
    private ArrayList<AssistantCard> generateAssistant(){

        calledAssistant = new ArrayList<>();
        ArrayList<Integer> numbersGenerated = new ArrayList<>();     //list containing numbers already generated
        Random randomGenerator = new Random();
        for(int i=0;i<3;i++){
            int randomDraw;
            do{
                randomDraw = randomGenerator.nextInt(13);
            }while(numbersGenerated.contains(randomDraw));         //check if the number is not already in the list
            numbersGenerated.add(randomDraw);

            AssistantCard generatedCard;

            switch (randomDraw){
                case 0:
                    generatedCard = new Assistant1();
                    break;
                case 1:
                    generatedCard = new Assistant2();
                    break;
                case 2:
                    generatedCard = new Assistant3();
                    break;
                case 3:
                    generatedCard = new Assistant4();
                    break;
                case 4:
                    generatedCard = new Assistant5();
                    break;
                case 5:
                    generatedCard = new Assistant6();
                    break;
                case 6:
                    generatedCard = new Assistant7();
                    break;
                case 7:
                    generatedCard = new Assistant8();
                    break;
                case 8:
                    generatedCard = new Assistant9();
                    break;
                case 9:
                    generatedCard = new Assistant10();
                    break;
                case 10:
                    generatedCard = new Assistant11();
                    break;
                case 11:
                    generatedCard = new Assistant12();
                    break;
                default:
                    generatedCard = null;

            }
            calledAssistant.add(generatedCard);
        }
        return calledAssistant;
    }

    /**Method to implements singleton
     * @return a list of three AssistantCard or call the generator if it doesn't exist
     * */
    public ArrayList<AssistantCard> drawAssistant(){
        if(calledAssistant == null){
            return generateAssistant();
        }
        else return calledAssistant;
    }
}
