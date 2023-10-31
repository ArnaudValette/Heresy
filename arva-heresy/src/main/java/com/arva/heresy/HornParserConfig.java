package com.arva.heresy;

class HornConfig {
    static String[][] config = {
            { "#,+,b,e,g,i,n,_", "startStructureTemplate" },
            { "#,+,e,n,d,_", "endStructureTemplate" },
            { "#,+,any,:, ", "orgDirective" },
            { "*, ", "heading" },
            { "-, ", "unorderedList" },
            { "digit,., ", "orderedList" },
            { "letter,., ", "orderedList" },
            { "TAB,-, ", "unorderedList" },
            { "TAB,digit,., ", "orderedList" },
            { "TAB,letter,., ", "orderedList" },
            { "|,tableText,|", "table" },
            { "-,-,-,-,-", "horizontalRule" },
            { ":, ", "orgCode" },
            { "[,f,n,:,any,]", "footNote" },
            { "C,L,O,C,K,:, ", "clock" },
    };
}
public class HornParserConfig extends ParserConfig{
    public HornParserConfig() {
        super(HornConfig.config);
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
        map.subscribeChars(" -.#+_:|[]CLOKefnbgid");
        return map;
    }
}

