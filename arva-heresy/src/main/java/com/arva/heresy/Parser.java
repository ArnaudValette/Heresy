package com.arva.heresy;


public class Parser {
    /* We should operate from there to spawn Threads
       Each step should give us a new kind of data for the next one
       HornParser should give the substring that is located after e.g. ** , - , and etc
       this substring is then the base of work for BracketParser.
       BracketParser gives us textDelimitations from which we
       want to reconstruct substring of the base work,
       in order for us to Format them.

       lines = File.readByLines();
       forEach line -> {
         * SomeThread
            line = lines[someIndex]
            const [type, text] = HornNode.parse(line);
            const [textDelimitations, brackets] = BracketParser.parse(line);
            const [textFragments] = reconstruct(line, textDelimitations)

            forEach textFragment -> {
              ** ChildrenThread
                textFragment = textFragments(subIndex);
                const [properFormattedTextNode] = FormatParser.parse(textFragment, line);
            }
       }
    */
    public Parser() {
    }
}

