package com.arva.heresy;

class HornConfig {
    static String[][] config = {
            { "*, ", "heading" },
            { "TAB,-, ", "unorderedList" },
            { "TAB,digit,., ", "unorderedList" },
            { "TAB,letter,., ", "unorderedList" },
            { "#,+,b,e,g,i,n,_,any, ", "startStructureTemplate" },
            { "#,+,e,n,d,_,any, ", "endStructureTemplate" },
            { "#,+,any,:, ", "orgDirective" },
            // note: Tables are not orgNodes
            { "|,tableText,|", "table" },
            //{ "|,dash,|", "tableSeparator" },
            { "-,-,-,-,-", "horizontalRule" },
            { ":, ", "orgCode" },
            { "[,f,n,:,any,], ", "footNote" },
            { "C,L,O,C,K,:, ", "clock" },
    };
}
public class HornParserConfig extends ParserConfig{
    public HornParserConfig() {
        super(HornConfig.config);
        this.describe();
    }
}
class HornParserCommands {
    static CommandMap generateCommands() {
        CommandMap map = new CommandMap();
        map.put("digit", c -> Character.isDigit(c), true);
        map.put("letter", c -> Character.isAlphabetic(c), false);
        map.put("*", c -> c == '*', true);
        map.put("TAB", c -> c == ' ', true);
        map.put("any", c -> c != ' ' && c != ']' && c != ':' , true);
        map.put("tableText", c -> c != '|' , true);

        map.subscribeChar(" -.#+_:|CLOKfnbegid");
        return map;
    }
}

