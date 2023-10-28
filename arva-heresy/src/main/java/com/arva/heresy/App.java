package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
            // BracketParser bracketParser = new BracketParser(new ParserConfig()) ;
            // FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            FormatParser formatParser = new FormatParser(new FormatParserConfig());
            List<Integer> l = new ArrayList<>();
            l.add(0);
            l.add(0);
            //FormatResult results = formatParser.parse("*ok /ok/* yes +/ _ ok", l);
            FormatResult results = formatParser.parse("ok *ok/* /yes/  _ ok", l);
            results.formats.describe();

    }
}
