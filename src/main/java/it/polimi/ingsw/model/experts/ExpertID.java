package it.polimi.ingsw.model.experts;

/**Type of use card method for experts, each constant is associated to a type of method in ExpertCard interface, user
 * must always be passed
 * @author Massimo*/
public enum ExpertID {
    /**Only user to pass*/
    USER_ONLY,
    /**User + island nodeID to pass*/
    NODE_ID,
    /**User + island nodeID + color to pass*/
    NODE_ID_COLOR,
    /**User + color to pass*/
    COLOR,
    /**User + 2 array list of color to pass*/
    TWO_LIST_COLOR
}
