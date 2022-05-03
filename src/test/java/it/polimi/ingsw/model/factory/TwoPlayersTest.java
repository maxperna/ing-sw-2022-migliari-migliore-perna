package it.polimi.ingsw.model.factory;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.model.factory.TwoPlayers.*;

class TwoPlayersTest {

    @Test
    void shouldCreateTwoPlayers() {

        TwoPlayers twoPlayersFactory = new TwoPlayers();
        ArrayList<Player> listOfPlayers;
        listOfPlayers = twoPlayersFactory.createPlayers();

        //Check Players
        Assertions.assertEquals(numberOfPlayers, listOfPlayers.size());


        for (int i = 0; i < numberOfPlayers; i++) {

            //Check Boards
            Assertions.assertNotNull(listOfPlayers.get(i).getBoard());

            //Check StudentHall
            Assertions.assertEquals(maxStudentHall, listOfPlayers.get(i).getBoard().getStudentsOutside().size());

            //Check Towers
            Assertions.assertEquals(maxTowers, listOfPlayers.get(i).getBoard().getTowers().size());

        }

        //Prints results
        System.out.println("numero di giocatori " + listOfPlayers.size());
        for (Player player : listOfPlayers) {
            System.out.println("-------- Player ----------------------------------------------------------------------------");
            for (int i = 0; i < player.getBoard().getStudentsOutside().size(); i++)
                System.out.println(player.getBoard().getStudentsOutside().get(i));
            for (int k = 0; k < player.getBoard().getTowers().size(); k++)
                System.out.println(player.getBoard().getTowers().get(k));
        }

    }

}