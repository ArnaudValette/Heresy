package com.arva.heresy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Parser {
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
    public void parse(File file) {
        BracketParser b = new BracketParser(new ParserConfig());
        FormatParser f = new FormatParser(new FormatParserConfig());
        List<BracketResult> bRes = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            AtomicInteger ln = new AtomicInteger(1);
            while((line = reader.readLine()) != null){
                final String lineToProcess = line;
                final int currLn = ln.getAndIncrement();
                BracketResult bracketResult = b.parse(lineToProcess, currLn);
                bRes.add(bracketResult);
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

