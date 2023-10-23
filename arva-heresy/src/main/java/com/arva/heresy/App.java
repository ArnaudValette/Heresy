package com.arva.heresy;


public class App 
{
    public static void main( String[] args )
    {
            FileProcessor.iterativeCall("/home/truite/journal/journal.org", System.out::println);
            ParserConfig config = new ParserConfig() ;
            config.describe();
    }
}
