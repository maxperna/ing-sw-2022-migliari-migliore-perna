package it.polimi.ingsw.model.assistants;

public class AssistantFactory {
    public AssistantCard getAssistant(Assistants assistantType){

        AssistantCard assistant = null;
        switch (assistantType){
            case MOVE_TO_ISLAND:
                assistant = new;
                break;
            case SAME_INFLUENCE_CONTROL:
                assistant = new;
                break;
            case ISLAND_CHOOSE_MOTHER_NATURE:
                assistant = new;
                break;
            case ADDITIONAL_MN_MOVING:
                assistant = new;
                break;
            case DENIES_KEEPER:
                assistant = new;
                break;
            case IGNORE_TOWER:
                assistant = new;
                break;
            case STUDENT_EXCHANGE:
                assistant = new;
                break;
            case ADD_INFLUENCE:
                assistant = new;
                break;
            case IGNORE_COLOR:
                assistant = new;
                break;
            case SWAP_HALL_STUDENT:
                assistant = new;
                break;
            case MOVE_TO_HALL:
                assistant = new;
                break;
            case REMOVE_COLOR_HALL:
                assistant = new;
                break;

        }
    }
}
