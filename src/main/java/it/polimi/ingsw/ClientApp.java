package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.JavaFXGui;
import javafx.application.Application;

public class ClientApp {
    public static void main(String[] args) {
        boolean Cli = false;

        for (String param : args) {
            if (param.equals("-c") || param.equals("--cli")) {
                Cli = true;
                break;
            }
        }

        if (Cli) {
            Cli cliView = new Cli();
            ClientController clientController = new ClientController(cliView);
            cliView.addListener(clientController);
            cliView.start();
        } else {
            Application.launch(JavaFXGui.class);
        }
    }

}
