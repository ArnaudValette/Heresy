package com.arva.heresy;

class BracketConfig{
    static String[][] config = {
            {"[,digit,%,]", "cookiePercent"},
            {"[,digit,/,digit,]","cookieRatio"},
            {"[,digit,-,digit,-,digit, ,Capital,low,low,]","date"},
            {"[, ,]","checkBoxEmpty"},
            {"[,X,]","checkBoxActive"},
            {"[,[,any,],]","image"},
            {"[,[,any,],[,ANY,],]","link"},
            {"[,f,n,:,any,]","footnote"},
                };
}

public class BracketParserConfig extends ParserConfig {
    final ParserConfigNode children = new Node();

    public BracketParserConfig() {
        super(BracketConfig.config);
    }
}

class BracketParserCommands {
    static CommandMap generateCommands(){
        CommandMap map = new CommandMap();
        map.put("digit", c -> Character.isDigit(c), true);
        map.put("Capital", c -> Character.isUpperCase(c) , false);
        map.put("low", c -> Character.isLowerCase(c) , false);
        map.put("any", c -> c != ' ' && c != '[' && c != ']', true);
        map.put("ANY", c -> c != '[' && c != ']', true);
        map.subscribeChar("%/- X[]fn:");
        return map;
    }
}

