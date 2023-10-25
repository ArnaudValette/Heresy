package com.arva.heresy;


//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
            BracketParser bracketParser = new BracketParser(new ParserConfig()) ;
            FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
    }
}
