package com.arva.heresy;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
            BracketParser bracketParser = new BracketParser(new ParserConfig()) ;
            //FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            // Testing on one string

            FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            // Gson g = new GsonBuilder().setPrettyPrinting().create();
            // String json = g.toJson(bracketParser.brackets);
            // String ok = g.toJson(bracketParser.config);
            // System.out.println(json);
            // System.out.println(ok);
    }
}
