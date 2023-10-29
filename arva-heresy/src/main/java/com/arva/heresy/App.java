package com.arva.heresy;

import java.io.File;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
        Parser parser = new Parser();
        parser.parse(new File("/home/truite/journal/test.org"));
        //FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            


    }
}
