package com.arva.heresy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Parser {
    List<HornResult> nodes;

    public CompletableFuture<List<HornResult>> parse(File file){
       List<CompletableFuture<HornResult>> futures = new ArrayList<>();
       HornParser parser = new HornParser();
        BracketParser b = new BracketParser();
        FormatParser f = new FormatParser(new FormatParserConfig());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            AtomicInteger ln = new AtomicInteger(1);
            while((line = reader.readLine()) != null){
                final String lineToProcess = line;
                final int currLn = ln.getAndIncrement();
                CompletableFuture<HornResult> future = CompletableFuture.supplyAsync(()->{
                    return  parser.parseHorns(lineToProcess, currLn, b, f);
                    }
                ,executor);
                futures.add(future);
            }
        }
        catch(FileNotFoundException e){
            System.err.println("File " + file.toString() + " not found");
        }
        catch(IOException e){
            System.err.println(e);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allOf.thenApply(v -> {
            return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
            }).whenComplete((res,ex)->executor.shutdown());
    }

    public void setReturnValue(List<HornResult> val) {
        nodes = val;
    }

    public List<HornResult> getResults() {
        return nodes;
    }
}

