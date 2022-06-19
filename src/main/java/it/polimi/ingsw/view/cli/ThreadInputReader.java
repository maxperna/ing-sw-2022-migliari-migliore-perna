package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ThreadInputReader implements Callable<String> {
    private final BufferedReader bufferedReader;


    public ThreadInputReader() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

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
