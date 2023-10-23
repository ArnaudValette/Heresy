package com.arva.heresy;


public class App 
{
    public static void main( String[] args )
    {
            BracketParser bracketParser = new BracketParser(new ParserConfig()) ;
            //FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            // Testing on one string
            FileProcessor.simpleCall("/home/truite/journal/journal.org", bracketParser::parse);
    }
}
