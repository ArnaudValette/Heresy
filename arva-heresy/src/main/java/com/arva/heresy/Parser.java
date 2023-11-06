package com.arva.heresy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Parser {
    List<HornResult> nodes;

    public CompletableFuture<List<HornResult>> parse(File file){
       List<CompletableFuture<HornResult>> futures = new ArrayList<>();
       HornParser parser = new HornParser();
        BracketParser b = new BracketParser();
        FormatParser f = new FormatParser(new FormatParserConfig());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // Store futures<HornResult> in futures
        FileProcessor.getHornResults(file, futures, executor, (s, i)->parser.parseHorns(s, i, b, f));

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

