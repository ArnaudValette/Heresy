package com.arva.heresy;

import java.io.File;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();
        Parser parser = new Parser();
        parser.parse(new File("/home/truite/journal/test3.org"));
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + " ms");
        //FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            


    }
}
