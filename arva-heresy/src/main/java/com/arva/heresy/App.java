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
            int offset= 5;
            l.add(offset);
            l.add(9);
            //FormatResult results = formatParser.parse("*ok /ok/* yes +/ _ ok", l);
            String teststr = "     */ok/* /*y_es*/ **_ ok*";
            FormatResult results = formatParser.parse(teststr.substring(offset), l);
            results.formats.describe();
            String x = teststr.substring(results.formats.get(0).start, results.formats.get(0).end);
            System.out.println(x);


    }
}
