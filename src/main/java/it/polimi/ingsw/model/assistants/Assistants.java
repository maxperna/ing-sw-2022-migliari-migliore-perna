package it.polimi.ingsw.model.assistants;

public enum Assistants {
    MOVE_TO_ISLAND,    //Take a student on this card and place on an island of your choice, then draw another one
    SAME_INFLUENCE_CONTROL, //During this turn you can take control of profs even with the same number of student
    ISLAND_CHOOSE_MOTHER_NATURE, //Chose an island and resolve it as if MotherN were moved there(move it normally anyway)
    ADDITIONAL_MN_MOVING, //You can move MN additionally of 2 steps during this turn
    DENIES_KEEPER,  //Place one deny card on an island and when MN finish her movement there don't calculate infl or build tower
    IGNORE_TOWER, //During this turn towers don't count as +1 influence
    STUDENT_EXCHANGE,  //Swap up to 3 students between this card and your entry hall
    ADD_INFLUENCE, //In this turn during inlf calculus you have additional 2 infl points
    IGNORE_COLOR, //Choose a color, during this turn it doesn't give influence
    SWAP_HALL_STUDENT,  //You can swap up to 2 student between your main hall and your entry hall
    MOVE_TO_HALL,   //Take a student on this card and put it in your main hall, then draw a new student
    REMOVE_COLOR_HALL;   //Choose a color, each player(including yourself) must return 3 students of that color form his main hall to the pouch
}
