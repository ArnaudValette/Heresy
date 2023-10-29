package com.arva.heresy;

import java.util.ArrayList;
import java.util.List;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class App 
{
    public static void main( String[] args )
    {
            // FileProcessor.iterativeCall("/home/truite/journal/journal.org", bracketParser::parse);
            BracketParser bracketParser = new BracketParser(new ParserConfig()) ;
            FormatParser formatParser = new FormatParser(new FormatParserConfig());
            String teststr = "[[x][y]]*/ok/* [[file][file]] /*y_es*/ [[img.png]] **_ ok*[[a]]";
            /*
              First, parse Brackets from a line
             */
            BracketResult resultBracket = bracketParser.parse(teststr, 1);
            /*
              Brackets gives us limits that are unformatted,
              for each of these limits, our bracketParser should parse
              a substring of our line.
             */
            resultBracket.toBeFormatted().forEach(lim -> {
                     String toProcess = teststr.substring(lim.get(0), lim.get(1));
                     FormatResult fres = formatParser.parse(toProcess, lim);
                     fres.formats.describe();
             });
            


    }
}
