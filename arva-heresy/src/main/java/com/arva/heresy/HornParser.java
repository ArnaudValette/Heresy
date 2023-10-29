package com.arva.heresy;

class HornResult {
    public HornResult() {
    }
}
public class HornParser extends TreeBasedParser {
    public HornParser() {
        super(new HornParserConfig(), HornParserCommands.generateCommands());
    }


    public HornResult parseHorns(String s, int index) {
        //System.out.println(s);

        return new HornResult();
    }
}
