package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Class that is used to manage the input from cli in a thread-safe mode through the use of a bufferedReader
 * @author Alessio Migliore
 */
public class ThreadInputReader implements Callable<String> {
    private final BufferedReader bufferedReader;


    /**
     * Default constructor
     */
    public ThreadInputReader() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Method that will read from input
     * @return the string read
     * @throws IOException if an error occurs
     * @throws InterruptedException when the thread is interrupted
     */
    @Override
    public String call() throws IOException, InterruptedException {
        String input;
        while (!bufferedReader.ready()) {
            Thread.sleep(200);
        }
        input = bufferedReader.readLine();
        return input;
    }
}
