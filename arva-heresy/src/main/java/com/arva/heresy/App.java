package com.arva.heresy;

import java.io.File;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
        Parser parser = new Parser();
        // for(int i = 0; i<20; i++){
        //     parser.parse(new File("/home/truite/journal/test3.org"));
        // }

        Runtime runtime = Runtime.getRuntime();
        long usedMemB = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        parser.parse(new File("/home/truite/journal/test3.org"));
        long usedMemE = runtime.totalMemory() - runtime.freeMemory();
        long memUse = usedMemE - usedMemB;
        double memoryUsedInKB = (double) memUse / 1024;
        double memoryUsedInMB = memoryUsedInKB / 1024;
        System.out.println("Memory Used (Bytes): " + memUse);
        System.out.println("Memory Used (KB): " + memoryUsedInKB);
        System.out.println("Memory Used (MB): " + memoryUsedInMB);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + " ms");
        //FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            


    }
}
