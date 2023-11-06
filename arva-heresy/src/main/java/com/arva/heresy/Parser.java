package com.arva.heresy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Parser {
    List<HornResult> nodes;

    public CompletableFuture<List<HornResult>> parse(File file){
       List<CompletableFuture<HornResult>> futures = new ArrayList<>();
       HornParser parser = new HornParser();
        BracketParser b = new BracketParser();
        FormatParser f = new FormatParser(new FormatParserConfig());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // Store futures<HornResult> in futures
        HornProcess.getHornResults(file, futures, executor, (s, i)->parser.parseHorns(s, i, b, f));

        return HornProcess.getCompletableFutures(futures, executor);
    }

    public void setReturnValue(List<HornResult> val) {
        nodes = val;
    }

    public List<HornResult> getResults() {
        return nodes;
    }
}

