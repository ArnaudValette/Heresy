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
    /* We should operate from there to spawn Threads
       Each step should give us a new kind of data for the next one
       HornParser should give the substring that is located after e.g. ** , - , and etc
       this substring is then the base of work for BracketParser.
       BracketParser gives us textDelimitations from which we
       want to reconstruct substring of the base work,
       in order for us to Format them.

       lines = File.readByLines();
       forEach line -> {
         * SomeThread
            line = lines[someIndex]
            const [type, text] = HornNode.parse(line);
            const [textDelimitations, brackets] = BracketParser.parse(line);
            const [textFragments] = reconstruct(line, textDelimitations)

            forEach textFragment -> {
              ** ChildrenThread
                textFragment = textFragments(subIndex);
                const [properFormattedTextNode] = FormatParser.parse(textFragment, line);
            }
       }
    */
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
                    HornResult hornResult = parser.parseHorns(lineToProcess, currLn);
                    hornResult.fillBracketsAndFormats(b, f, lineToProcess, currLn);
                    // hornResult.toBeFormatted().forEach(lim -> {
                    //     String toBracket = lineToProcess.substring(lim.get(0), lim.get(1));
                    //     BracketResult bracketResult = b.parseBrackets(toBracket, currLn);
                    //     bracketResult.toBeFormatted().forEach((li) -> {
                    //         String toFormat = toBracket.substring(li.get(0), li.get(1));
                    //         f.parse(toFormat, li);
                    //     });
                    // });
                    return hornResult;

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

    public void parse1(File file) {
        BracketParser b = new BracketParser();
        FormatParser f = new FormatParser(new FormatParserConfig());
        List<BracketResult> bRes = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            AtomicInteger ln = new AtomicInteger(1);
            while((line = reader.readLine()) != null){
                final String lineToProcess = line;
                final int currLn = ln.getAndIncrement();
                BracketResult bracketResult = b.parseBrackets(lineToProcess, currLn);
                bRes.add(bracketResult);
                bracketResult.nodes.describe();
                bracketResult.toBeFormatted().forEach(lim ->{
                        String toFormat = lineToProcess.substring(lim.get(0), lim.get(1));
                        FormatResult formatResult = f.parse(toFormat, lim);
                        formatResult.formats.describe();
                    });
            }
        }
        catch(FileNotFoundException e){
            System.err.println("File " + file.toString() + " not found");
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}

