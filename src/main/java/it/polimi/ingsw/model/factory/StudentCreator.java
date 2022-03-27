package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.TowerColor;

import java.util.ArrayList;

public interface StudentCreator {

    ArrayList<Player> createPlayers();
}
