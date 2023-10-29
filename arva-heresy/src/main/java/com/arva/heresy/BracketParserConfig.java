package com.arva.heresy;

class BracketConfig{
    static String[][] config = {
            {"[,digit,%", "cookiePercent"},
            {"[,digit,/,digit","cookieRatio"},
            {"[,digit,-,digit,-,digit, ,Capital,low,low","date"},
            {"[, ","checkBoxEmpty"},
            {"[,X","checkBoxActive"},
            {"[,[,any,]","image"},
            {"[,[,any,],[,ANY,]","link"},
            {"[,f,n,:,any","footnote"},
                };
}
public class BracketParserConfig extends ParserConfig {
    final ParserConfigNode children = new Node();

    public BracketParserConfig() {
        super(BracketConfig.config);
    }
}

