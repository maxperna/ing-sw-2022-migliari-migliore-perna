package it.polimi.ingsw.model.assistants;

import it.polimi.ingsw.exceptions.NotOnBoardException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.IslandTile;

import java.util.ArrayList;

public class StudentKeeper {
    private ArrayList<Color> cardStudents;

    public void addStudent(Color student) {
        this.cardStudents.add(student);
    }

    public Color pickStudent(Color student) throws NotOnBoardException{
        if(this.cardStudents.remove(student)){
            return student;
        }
        else throw new NotOnBoardException("Student not on card");

    }

    public void applyEffect(Color student, IslandTile isleToMove){
        try{
            isleToMove.addStudent(pickStudent(student));
            //addStudent();
        }
        catch( NotOnBoardException e){
            e.printStackTrace();
        }


    }
}
