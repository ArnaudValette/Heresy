package com.arva.heresy;

class HornResult {
    public HornResult() {
    }
}
public class HornParser {
    HornParserConfig config;
    CommandMap commands;
    public HornParser(HornParserConfig conf) {
        config = conf;
        commands = generateCommands();
    }

    public CommandMap generateCommands() {
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

    public HornResult parse(String s, int index) {
        //System.out.println(s);

        return new HornResult();
    }
}
