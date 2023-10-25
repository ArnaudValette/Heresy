package com.arva.heresy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
interface StringFunction {
    Result apply(String s, int i);
}

class Result {
    BracketNodes brackets;
    int lineNumber;

    public Result(BracketNodes b, int i) {
        this.brackets = b;
        this.lineNumber = i;
    }
}

public class FileProcessor {

    public static void iterativeCall(String pathName, StringFunction f) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Result>> futures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName))) {
            String line;
            AtomicInteger lineNumber = new AtomicInteger(1);
            while ((line = reader.readLine()) != null) {
                final String lineToProcess = line;
                final int currentLineNumber = lineNumber.getAndIncrement();
                futures.add(executor.submit(() -> f.apply(lineToProcess, currentLineNumber)));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            executor.shutdown();
        }

        List<Result> results = new ArrayList<>();
        for (Future<Result> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        results.sort(Comparator.comparingInt(result -> result.lineNumber));
        results.forEach((res) -> {
                res.brackets.describe();
        });
    }
}
