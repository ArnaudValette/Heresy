package com.arva.heresy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@FunctionalInterface
interface BracketFunction {
    BracketResult apply(String s, int i);
}

@FunctionalInterface
interface FormatFunction {
    FormatResult apply(String s, int i);
}

@FunctionalInterface
interface FileProcessorCallback {
    HornResult apply(String s, int i);
}

public class HornProcess {

    public static CompletableFuture<List<HornResult>> getCompletableFutures(List<CompletableFuture<HornResult>> list, ExecutorService executor ) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        return allOf.thenApply(v->{
                return list.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            }).exceptionally(e->{e.printStackTrace(); return null;})
                .whenComplete((res, ex) -> executor.shutdown());
    }

    public static void getHornResults(File file, List<CompletableFuture<HornResult>> list, ExecutorService executor, FileProcessorCallback callBack) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            AtomicInteger ln = new AtomicInteger(1);
            while ((line = reader.readLine()) != null) {
                final String lineToProcess = line;
                final int currLn = ln.getAndIncrement();
                list.add(CompletableFuture.supplyAsync(()->{
                    return callBack.apply(lineToProcess, currLn);
                }, executor));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
